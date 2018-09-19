/**
 * 
 */
package com.cs.baseapp.api.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cs.baseapp.api.appenv.AppEnv;
import com.cs.baseapp.api.appenv.AppEnvFactory;
import com.cs.baseapp.api.base.AppBaseFactory;
import com.cs.baseapp.api.base.Base;
import com.cs.baseapp.api.config.Configuration;
import com.cs.baseapp.api.filter.FilterFactory;
import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.api.messagebroker.MessageBroker;
import com.cs.baseapp.api.messagebroker.MessageBrokerFactory;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
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

	private static List<MessageFilter> filters = new ArrayList<>();

	private static MessageBroker mb;

	private static AppEnv appEnv;

	private static ServiceLogKey logKey = LogManager.getServiceLogKey();

	private static final Logger logger = Logger.getLogger("SYSTEM");

	private MSBaseApplication() {
	}

	public static void init(String configFile) throws BaseAppException {
		logger.info(logKey, "Start to loading the configuration. FilePath:" + configFile);
		try (FileInputStream fis = new FileInputStream(new File(configFile))) {
			init(fis);
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_00001", configFile));
		}
	}

	public static AppEnv getAppEnv() {
		return appEnv;
	}

	public static void init(InputStream is) throws BaseAppException {
		Configuration config = new Configuration();
		config.load(is);
		LogManager.init(config.getBaseConfig().get("name"));
		logger.info(logKey, "Start to parse the configuration.");
		base = AppBaseFactory.buildBase(config.getBaseConfig());
		logger.info(logKey, "Build Base success.");
		appEnv = AppEnvFactory.build(config.getEnvConfig());
		logger.info(logKey, "Build App Env success.");
		filters = FilterFactory.buildWebFilters(config.getFilterConfig());
		logger.info(logKey, "Build Web Filters success. Total:" + filters.size());
		mb = MessageBrokerFactory.buildMsgBroker(config.getMbSendersConfig(), config.getMbReceiversConfig(),
				config.getMbListenersConfig(), config.getMbLocalServicesConfig(), config.getMbRemoteServicesConfig());
		logger.info(logKey, "Build Message Broker Success.");
	}

	public static Base getBaseInfo() {
		return base;
	}

	public static void doWebFilters(MessageRequest csReqMsg, ServletRequest request, ServletResponse response,
			FilterChain chain) throws BaseAppException {
		try {
			logger.debug(LogManager.getServiceLogKey(csReqMsg),
					"Start to do web filter. RequestMsg:" + csReqMsg.getJsonObject());
			for (MessageFilter f : filters) {
				f.doWebFilter(csReqMsg, request, response, chain);
			}
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_00002", csReqMsg.getJsonString()));
		}
	}

	public static MessageBroker getMessageBroker() {
		return mb;
	}

	public static void shutdown() {
		mb.shutdown();
	}

}
