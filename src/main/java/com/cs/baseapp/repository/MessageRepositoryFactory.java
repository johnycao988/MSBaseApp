/**
 * 
 */
package com.cs.baseapp.repository;

import java.util.Map;
import java.util.Properties;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class MessageRepositoryFactory {

	private static Logger logger = LogManager.getSystemLog();

	private MessageRepositoryFactory() {

	}

	@SuppressWarnings("unchecked")
	public static BaseMessageRepository buildMessageRepository(Map<String, Object> repositoryConfig) {
		String implClass = (String) repositoryConfig.get(ConfigConstant.IMPL_CLASS.getValue());
		BaseMessageRepository instance = null;
		try {
			instance = (BaseMessageRepository) Class.forName(implClass).getConstructor(Properties.class)
					.newInstance(PropertiesUtils.convertMapToProperties(
							(Map<String, String>) repositoryConfig.get(ConfigConstant.PARAMETERS.getValue())));
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0039", implClass));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
		return instance;
	}

}
