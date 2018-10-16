/**
 * 
 */
package com.cs.baseapp.api.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.messagebroker.entity.MSMessageSender;
import com.cs.baseapp.api.messagebroker.pool.ObjectPool;
import com.cs.baseapp.api.messagebroker.pool.PoolObjectFactory;
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
public class SenderManager {

	private Map<String, ObjectPool<MSMessageSender>> pooledSenders = new HashMap<>();
	private Logger logger = LogManager.getSystemLog();

	public SenderManager(List<Map<String, Object>> sendersConfig) {
		try {
			if (sendersConfig == null) {
				throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0012"));
			}
			for (Map<String, Object> config : sendersConfig) {
				ObjectPool<MSMessageSender> pool = new ObjectPool<>(
						(int) config.get(ConfigConstant.POOL_SZIE.getValue()), new SenderFactory(config));
				pooledSenders.put((String) config.get(ConfigConstant.ID.getValue()), pool);
			}
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0012"));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}

	public MSMessageSender getSender(String id) throws BaseAppException {
		return this.pooledSenders.get(id).getObject();
	}

	public void releaseSender(MSMessageSender sender) {
		this.pooledSenders.get(sender.getId()).releaseObject(sender);
	}

}

class SenderFactory implements PoolObjectFactory<MSMessageSender> {

	private Map<String, Object> config;

	private ObjectPool<MSMessageSender> pool;

	public SenderFactory(Map<String, Object> config) {
		this.config = config;
	}

	public void setPool(ObjectPool<MSMessageSender> pool) {
		this.pool = pool;
	}

	@Override
	public MSMessageSender createPoolObject() throws BaseAppException {
		boolean isPooled = (int) this.config.get(ConfigConstant.POOL_SZIE.getValue()) > 1;
		MSMessageSender msMsgSendernew = new MSMessageSender(buildSingleMsgSender(this.config), isPooled, this.pool);
		msMsgSendernew.initialize();
		return msMsgSendernew;
	}

	@SuppressWarnings("unchecked")
	private static MessageSender buildSingleMsgSender(Map<String, Object> singleConfig) throws BaseAppException {
		MessageSender sender = null;
		try {
			sender = (MessageSender) Class.forName((String) singleConfig.get(ConfigConstant.IMPL_CLASS.getValue()))
					.getConstructor(String.class, Properties.class)
					.newInstance((String) singleConfig.get(ConfigConstant.ID.getValue()),
							(Properties) PropertiesUtils.convertMapToProperties(
									(Map<String, String>) singleConfig.get(ConfigConstant.PARAMETERS.getValue())));
		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0013", singleConfig.get(ConfigConstant.ID.getValue())));
		}
		return sender;
	}

}