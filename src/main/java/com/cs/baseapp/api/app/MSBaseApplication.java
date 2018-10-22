/**
 * 
 */
package com.cs.baseapp.api.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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
import com.cs.baseapp.utils.MSBaseAppStatus;
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

	private static List<BaseMessageFilter> filters = Collections.synchronizedList(new ArrayList<BaseMessageFilter>());

	private static MessageBroker mb;

	private static AppEnv appEnv;

	private static ServiceLogKey logKey = LogManager.getServiceLogKey();

	private static Logger logger = LogManager.getSystemLog();

	private static int status = MSBaseAppStatus.STOPPED.getValue();

	private static final String ERR_CODE_0051 = "ERR_0051";

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

	public static AppEnv getAppEnv() throws BaseAppException {
		if (status != MSBaseAppStatus.RUNNING.getValue()) {
			BaseAppException ex = new BaseAppException(LogInfoMgr.getErrorInfo(ERR_CODE_0051));
			logger.write(logKey, ex);
			throw ex;
		}
		return appEnv;
	}

	public static void init(InputStream is) throws BaseAppException {
		if (status == MSBaseAppStatus.RUNNING.getValue()) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0050"));
		}
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
			status = MSBaseAppStatus.RUNNING.getValue();
			logger.info(logKey, "Build Message Broker Success.");
		} catch (Exception e) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0035"));
		}
	}

	public static Base getBaseInfo() throws BaseAppException {
		if (status != MSBaseAppStatus.RUNNING.getValue()) {
			BaseAppException ex = new BaseAppException(LogInfoMgr.getErrorInfo(ERR_CODE_0051));
			logger.write(logKey, ex);
			throw ex;
		}
		return base;
	}

	public static int getStatus() {
		return status;
	}

	public static void doWebFilters(MessageRequest csReqMsg, ServletRequest request, ServletResponse response)
			throws BaseAppException {
		if (status != MSBaseAppStatus.RUNNING.getValue()) {
			BaseAppException ex = new BaseAppException(LogInfoMgr.getErrorInfo(ERR_CODE_0051));
			logger.write(logKey, ex);
			throw ex;
		}
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

	public static MessageBroker getMessageBroker() throws BaseAppException {
		if (status != MSBaseAppStatus.RUNNING.getValue()) {
			BaseAppException ex = new BaseAppException(LogInfoMgr.getErrorInfo(ERR_CODE_0051));
			logger.write(logKey, ex);
			throw ex;
		}
		return mb;
	}

	public static void shutdown() throws BaseAppException {
		if (status == MSBaseAppStatus.STOPPED.getValue() || status == MSBaseAppStatus.STOPPING.getValue()) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0052"));
		}
		logger.info(logKey, "Start to stopping the MSBaseApplication!");
		status = MSBaseAppStatus.STOPPING.getValue();
		filters.clear();
		logger.info(logKey, "The message filter stop success!");
		mb.shutdown();
		logger.info(logKey, "The message broker stop success!");
		status = MSBaseAppStatus.STOPPED.getValue();
		logger.info(logKey, "MSBaseApplication stop success!");
	}

	public static BaseMessageRepository getMsgRepository() throws BaseAppException {
		if (status != MSBaseAppStatus.RUNNING.getValue()) {
			BaseAppException ex = new BaseAppException(LogInfoMgr.getErrorInfo(ERR_CODE_0051));
			logger.write(logKey, ex);
			throw ex;
		}
		return mb.getMessageRepository();
	}

}
