/**
 * 
 */
package com.cs.baseapp.api.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cs.baseapp.api.appenv.AppEnv;
import com.cs.baseapp.api.appenv.AppEnvFactory;
import com.cs.baseapp.api.base.AppBaseFactory;
import com.cs.baseapp.api.base.Base;
import com.cs.baseapp.api.config.Configuration;
import com.cs.baseapp.api.filter.BaseMessageFilter;
import com.cs.baseapp.api.filter.FilterFactory;
import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.api.messagebroker.MessageBroker;
import com.cs.baseapp.api.messagebroker.MessageBrokerFactory;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.repository.BaseMessageRepository;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;
import com.cs.log.logs.bean.ServiceLogKey;

/**
 * @author Donald.Wang
 *
 */
public class MSBaseApplication {

	private static Base base;

	private static List<BaseMessageFilter> filters = new ArrayList<>();

	private static MessageBroker mb;

	private static AppEnv appEnv;

	private static ServiceLogKey logKey = LogManager.getServiceLogKey();

	private static final Logger logger = LogManager.getSystemLog();

	private MSBaseApplication() {
	}

	public static void init(String configFile) throws BaseAppException {
		logger.info(logKey, "Start to loading the configuration. FilePath:" + configFile);
		try (FileInputStream fis = new FileInputStream(new File(configFile))) {
			init(fis);
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0002", configFile));
		}
	}

	public static AppEnv getAppEnv() {
		return appEnv;
	}

	public static void init(InputStream is) throws BaseAppException {
		try {
			Configuration config = new Configuration();
			logger.info(logKey, "Start to parse the configuration.");
			config.load(is);
			LogManager.init(config.getBaseConfig().get(ConfigConstant.BASE_NAME.getValue()));
			logger.info(logKey, "Init LogManager success.");
			logger.info(logKey, "build Message Repository success.");
			base = AppBaseFactory.buildBase(config.getBaseConfig());
			logger.info(logKey, "Build Base success.");
			appEnv = AppEnvFactory.build(config.getEnvConfig());
			logger.info(logKey, "Build App Env success.");
			filters = FilterFactory.buildWebFilters(config.getFilterConfig());
			logger.info(logKey, "Build Web Filters success. Total:" + filters.size());
			mb = MessageBrokerFactory.buildMsgBroker(config.getMbSendersConfig(), config.getMbReceiversConfig(),
					config.getMbListenersConfig(), config.getMbLocalServicesConfig(),
					config.getMbRemoteServicesConfig(),
					PropertiesUtils.convertMapToProperties(config.getRepositoryConfig()));
			logger.info(logKey, "Build Message Broker Success.");
		} catch (Exception e) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0035"));
		}
	}

	public static Base getBaseInfo() {
		return base;
	}

	public static void doWebFilters(MessageRequest csReqMsg, ServletRequest request, ServletResponse response)
			throws BaseAppException {
		if (csReqMsg == null) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0001"));
		}
		logger.debug(LogManager.getServiceLogKey(csReqMsg),
				"Start to do web filter. RequestMsg:" + csReqMsg.getJsonObject());
		for (MessageFilter f : filters) {
			try {
				f.doWebFilter(csReqMsg, request, response);
			} catch (Exception e) {
				BaseAppException ex = new BaseAppException(e,
						LogInfoMgr.getErrorInfo("ERR_00002", csReqMsg.getJsonString()));
				logger.write(logKey, ex);
			}
		}
	}

	public static MessageBroker getMessageBroker() {
		return mb;
	}

	public static void shutdown() {
		mb.shutdown();
	}

	public static BaseMessageRepository getMsgRepository() {
		return mb.getMessageRepository();
	}

}
