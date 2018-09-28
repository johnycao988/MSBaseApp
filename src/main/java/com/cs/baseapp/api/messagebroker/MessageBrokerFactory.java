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

	private static final Logger logger = Logger.getLogger("SYSTEM");

	public static final int LOCAL_SERVICE = 0;

	public static final int REMOTE_SERVICE = 1;

	public static MessageBroker buildMsgBroker(List<Map<String, Object>> senderConfig,
			List<Map<String, Object>> receiverConfig, List<Map<String, Object>> listenerConfig,
			List<Map<String, Object>> localServiceConfig, List<Map<String, Object>> remoteServiceConfig)
			throws BaseAppException {
		logger.info(logKey, "Start to build Base App MessageBroker.");
		return new MessageBrokerEntity(senderConfig, receiverConfig, buildListeners(listenerConfig),
				buildServices(localServiceConfig, remoteServiceConfig));
	}

	private static Map<String, MessageListener> buildListeners(List<Map<String, Object>> listenersConfig)
			throws BaseAppException {
		logger.info(logKey, "Start to build MB Listener.");
		Map<String, MessageListener> listeners = new HashMap<>();
		for (Map<String, Object> singleConfig : listenersConfig) {
			MessageListener l = buildSingleListener(singleConfig);
			listeners.put(l.getId(), l);
		}
		logger.info(logKey, "Build MB Listeners success. Total:" + listeners.size());
		return listeners;
	}

	@SuppressWarnings("unchecked")
	private static MessageListener buildSingleListener(Map<String, Object> singleConfig) throws BaseAppException {
		MessageListener listener = null;
		try {
			listener = (MessageListener) Class.forName((String) singleConfig.get(ConfigConstant.IMPL_CLASS.getValue()))
					.getConstructor(String.class, int.class, Properties.class, MessageFilter.class)
					.newInstance((String) singleConfig.get(ConfigConstant.ID.getValue()),
							(int) singleConfig.get(ConfigConstant.MAX_PROCESS_THREADS.getValue()),
							(Properties) PropertiesUtils.convertMapToProperties(
									(Map<String, String>) singleConfig.get(ConfigConstant.PARAMETERS.getValue())),
							FilterFactory.buildListenerFilter(singleConfig));
			logger.info(logKey,
					"Build MB Listener success. ListenerId:" + singleConfig.get(ConfigConstant.ID.getValue())
							+ ConfigConstant.IMPL_CLASS.getValue() + ":"
							+ singleConfig.get(ConfigConstant.IMPL_CLASS.getValue()) + " MaxProcessThreads:"
							+ singleConfig.get(ConfigConstant.MAX_PROCESS_THREADS.getValue()));
		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0010", singleConfig.get(ConfigConstant.ID.getValue())));
		}
		return listener;
	}

	private static Map<String, MBService> buildServices(List<Map<String, Object>> loaclServicesConfig,
			List<Map<String, Object>> remoteServicesConfig) throws BaseAppException {
		Map<String, MBService> services = new HashMap<>();
		logger.info(logKey, "Start to build MB Service.");
		logger.info(logKey, "Start to build MB Local Service.");
		for (Map<String, Object> config : loaclServicesConfig) {
			MBService m = buildService(config, LOCAL_SERVICE);
			services.put(m.getId(), m);
		}
		logger.info(logKey, "Start to build MB Remote Service.");
		for (Map<String, Object> config : remoteServicesConfig) {
			MBService m = buildService(config, REMOTE_SERVICE);
			services.put(m.getId(), m);
		}
		logger.info(logKey, "Build MB Services success. Total:" + services.size());
		return services;
	}

	@SuppressWarnings("unchecked")
	private static MBService buildService(Map<String, Object> serviceConfig, int serviceType) throws BaseAppException {
		Map<String, String> config = new HashMap<>();
		config.put("serviceType", String.valueOf(serviceType));
		Set<Entry<String, Object>> set = serviceConfig.entrySet();
		Properties prop = PropertiesUtils
				.convertMapToProperties((Map<String, String>) serviceConfig.get(ConfigConstant.PARAMETERS.getValue()));
		serviceConfig.remove(ConfigConstant.PARAMETERS.getValue());
		for (Entry<String, Object> e : set) {
			config.put(e.getKey(), (String) e.getValue());
		}
		logger.info(logKey, "Build MB Service success. ServiceId:" + serviceConfig.get("id") + "ServiceType:"
				+ (serviceType == LOCAL_SERVICE ? "Local" : "Remote"));
		return new ServiceEntity(config, prop);
	}

}
