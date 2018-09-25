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
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class SenderManager {

	private Map<String, ObjectPool<MSMessageSender>> pooledSenders = new HashMap<>();

	public SenderManager(List<Map<String, Object>> sendersConfig) {
		for (Map<String, Object> config : sendersConfig) {
			pooledSenders.put((String) config.get("id"),
					new ObjectPool<MSMessageSender>((int) config.get("poolSize"), new SenderFactory(config)));
		}
	}

	public MSMessageSender getSender(String id) throws InterruptedException, BaseAppException {
		return this.pooledSenders.get(id).getObject();
	}

	public void releaseSender(MSMessageSender sender) {
		this.pooledSenders.get(sender.getId()).releaseObject(sender);
	}

}

class SenderFactory implements PoolObjectFactory<MSMessageSender> {

	private Map<String, Object> config;

	public SenderFactory(Map<String, Object> config) {
		this.config = config;
	}

	@Override
	public MSMessageSender createPoolObject() throws BaseAppException {
		boolean isPooled = (int) this.config.get("poolSize") > 1;
		MSMessageSender msMsgSendernew = new MSMessageSender(buildSingleMsgSender(this.config), isPooled);
		msMsgSendernew.initialize();
		return msMsgSendernew;
	}

	@SuppressWarnings("unchecked")
	private static MessageSender buildSingleMsgSender(Map<String, Object> singleConfig) throws BaseAppException {
		MessageSender sender = null;
		try {
			sender = (MessageSender) Class.forName((String) singleConfig.get("implementClass"))
					.getConstructor(String.class, Properties.class)
					.newInstance((String) singleConfig.get("id"), (Properties) PropertiesUtils
							.convertMapToProperties((Map<String, String>) singleConfig.get("parameters")));
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0008", singleConfig.get("id")));
		}
		return sender;
	}

}