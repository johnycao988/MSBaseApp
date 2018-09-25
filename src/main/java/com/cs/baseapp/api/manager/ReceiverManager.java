/**
 * 
 */
package com.cs.baseapp.api.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cs.baseapp.api.messagebroker.MessageReceiver;
import com.cs.baseapp.api.messagebroker.entity.MSMessageReceiver;
import com.cs.baseapp.api.messagebroker.pool.ObjectPool;
import com.cs.baseapp.api.messagebroker.pool.PoolObjectFactory;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class ReceiverManager {

	private Map<String, ObjectPool<MSMessageReceiver>> pooledReceivers = new HashMap<>();;

	public ReceiverManager(List<Map<String, Object>> configs) {
		try {
			for (Map<String, Object> config : configs) {
				ObjectPool<MSMessageReceiver> pool = new ObjectPool<>((int) config.get("poolSize"),
						new ReceiverFactory(config));
				pooledReceivers.put((String) config.get("id"), pool);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MSMessageReceiver getById(String id) throws BaseAppException, InterruptedException {
		return this.pooledReceivers.get(id).getObject();
	}

	public void release(MSMessageReceiver receiver) {
		this.pooledReceivers.get(receiver.getId()).releaseObject(receiver);
	}

}

class ReceiverFactory implements PoolObjectFactory<MSMessageReceiver> {

	private Map<String, Object> config;

	public ReceiverFactory(Map<String, Object> config) {
		this.config = config;
	}

	@Override
	public MSMessageReceiver createPoolObject() throws BaseAppException {
		boolean isPooled = (int) this.config.get("poolSize") > 1;
		MSMessageReceiver receiver = new MSMessageReceiver(buildSingleReceiver(this.config), isPooled);
		receiver.initialize();
		return receiver;
	}

	@SuppressWarnings("unchecked")
	private static MessageReceiver buildSingleReceiver(Map<String, Object> singleConfig) throws BaseAppException {
		MessageReceiver receiver = null;
		try {
			receiver = (MessageReceiver) Class.forName((String) singleConfig.get("implementClass"))
					.getConstructor(String.class, Properties.class)
					.newInstance((String) singleConfig.get("id"), (Properties) PropertiesUtils
							.convertMapToProperties((Map<String, String>) singleConfig.get("parameters")));

		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0009", singleConfig.get("id")));
		}
		return receiver;
	}

}
