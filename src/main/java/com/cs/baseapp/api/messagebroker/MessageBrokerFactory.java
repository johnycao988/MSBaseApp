/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.cs.baseapp.api.filter.FilterFactory;
import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.api.messagebroker.entity.MessageBrokerEntity;
import com.cs.baseapp.api.messagebroker.entity.ServiceEntity;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.repository.BaseMessageRepository;
import com.cs.baseapp.repository.DefaultMessageRepository;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;
import com.cs.log.logs.bean.ServiceLogKey;

/**
 * @author Donald.Wang
 *
 */
public class MessageBrokerFactory {

	private MessageBrokerFactory() {

	}

	private static ServiceLogKey logKey = LogManager.getServiceLogKey();

	private static final Logger logger = LogManager.getSystemLog();

	public static final int LOCAL_SERVICE = 0;

	public static final int REMOTE_SERVICE = 1;

	public static MessageBroker buildMsgBroker(List<Map<String, Object>> senderConfig,
			List<Map<String, Object>> receiverConfig, List<Map<String, Object>> listenerConfig,
			List<Map<String, Object>> localServiceConfig, List<Map<String, Object>> remoteServiceConfig,
			Properties repositoryConfig) throws BaseAppException {
		logger.info(logKey, "Start to build Base App MessageBroker.");
		return new MessageBrokerEntity(senderConfig, receiverConfig, buildListeners(listenerConfig),
				buildServices(localServiceConfig, remoteServiceConfig), buildMessageRepository(repositoryConfig));
	}

	private static Map<String, BaseMessageListener> buildListeners(List<Map<String, Object>> listenersConfig)
			throws BaseAppException {
		logger.info(logKey, "Start to build MB Listener.");
		Map<String, BaseMessageListener> listeners = new HashMap<>();
		if (listenersConfig == null || listenersConfig.isEmpty()) {
			return listeners;
		}
		for (Map<String, Object> singleConfig : listenersConfig) {
			BaseMessageListener l = buildSingleListener(singleConfig);
			if (l != null) {
				listeners.put(l.getId(), l);
			}
		}
		logger.info(logKey, "Build MB Listeners success. Total:" + listeners.size());
		return listeners;
	}

	@SuppressWarnings("unchecked")
	private static BaseMessageListener buildSingleListener(Map<String, Object> singleConfig) throws BaseAppException {
		BaseMessageListener listener = null;
		if (singleConfig == null || singleConfig.isEmpty()) {
			return listener;
		}
		String implClass = (String) singleConfig.get(ConfigConstant.IMPL_CLASS.getValue());
		String listenerId = (String) singleConfig.get(ConfigConstant.ID.getValue());
		try {
			listener = (BaseMessageListener) Class.forName(implClass)
					.getConstructor(String.class, int.class, Properties.class, MessageFilter.class, int.class,
							String.class)
					.newInstance(listenerId, (int) singleConfig.get(ConfigConstant.MAX_PROCESS_THREADS.getValue()),
							(Properties) PropertiesUtils.convertMapToProperties(
									(Map<String, String>) singleConfig.get(ConfigConstant.PARAMETERS.getValue())),
							FilterFactory.buildListenerFilters((List<Map<String, Object>>) singleConfig
									.get(ConfigConstant.MESSAGE_FILTER.getValue())),
							(int) singleConfig.get(ConfigConstant.CONNECTIONS.getValue()),
							(String) singleConfig.get(ConfigConstant.TRANS_CLASS.getValue()));
			logger.info(logKey, "Build MB Listener success. ListenerId:" + listenerId
					+ ConfigConstant.IMPL_CLASS.getValue() + ":" + implClass);
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0016", listenerId));
			logger.write(logKey, ex);
		}
		return listener;
	}

	private static Map<String, MBService> buildServices(List<Map<String, Object>> loaclServicesConfig,
			List<Map<String, Object>> remoteServicesConfig) {
		Map<String, MBService> services = new HashMap<>();
		logger.info(logKey, "Start to build MB Local Service.");
		if (loaclServicesConfig != null) {
			for (Map<String, Object> config : loaclServicesConfig) {
				MBService m = buildService(config, LOCAL_SERVICE);
				if (m != null) {
					services.put(m.getId(), m);
				}
			}
		}
		logger.info(logKey, "Start to build MB Remote Service.");
		if (remoteServicesConfig != null) {
			for (Map<String, Object> config : remoteServicesConfig) {
				MBService m = buildService(config, REMOTE_SERVICE);
				if (m != null) {
					services.put(m.getId(), m);
				}
			}
		}
		logger.info(logKey, "Build MB Services success. Total:" + services.size());
		return services;
	}

	@SuppressWarnings("unchecked")
	private static MBService buildService(Map<String, Object> serviceConfig, int serviceType) {
		ServiceEntity serviceEntity = null;
		Map<String, String> config = new HashMap<>();
		try {
			config.put("serviceType", String.valueOf(serviceType));
			Properties prop = PropertiesUtils.convertMapToProperties(
					(Map<String, String>) serviceConfig.get(ConfigConstant.PARAMETERS.getValue()));
			boolean storeMsg = serviceConfig.get(ConfigConstant.STORE_MSG.getValue()) != null
					&& (boolean) serviceConfig.get(ConfigConstant.STORE_MSG.getValue());
			serviceConfig.remove(ConfigConstant.PARAMETERS.getValue());
			serviceConfig.remove(ConfigConstant.STORE_MSG.getValue());
			Set<Entry<String, Object>> set = serviceConfig.entrySet();
			for (Entry<String, Object> e : set) {
				config.put(e.getKey(), String.valueOf(e.getValue()));
			}
			serviceEntity = new ServiceEntity(config, prop, storeMsg);
			logger.info(logKey,
					"Build MB Service success. ServiceId:" + serviceConfig.get(ConfigConstant.ID.getValue()));
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0017", serviceConfig.get(ConfigConstant.ID.getValue())));
			logger.write(logKey, ex);
		}
		return serviceEntity;
	}

	private static BaseMessageRepository buildMessageRepository(Properties reponsitoryConfig) {
		return new DefaultMessageRepository(reponsitoryConfig);
	}

}
