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
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class ReceiverManager {

	private Map<String, ObjectPool<MSMessageReceiver>> pooledReceivers = new HashMap<>();

	public ReceiverManager(List<Map<String, Object>> configs) {
		try {
			for (Map<String, Object> config : configs) {
				ObjectPool<MSMessageReceiver> pool = new ObjectPool<>(
						(int) config.get(ConfigConstant.POOL_SZIE.getValue()), new ReceiverFactory(config));
				pooledReceivers.put((String) config.get(ConfigConstant.ID.getValue()), pool);
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
		boolean isPooled = (int) this.config.get(ConfigConstant.POOL_SZIE.getValue()) > 1;
		MSMessageReceiver receiver = new MSMessageReceiver(buildSingleReceiver(this.config), isPooled);
		receiver.initialize();
		return receiver;
	}

	@SuppressWarnings("unchecked")
	private static MessageReceiver buildSingleReceiver(Map<String, Object> singleConfig) throws BaseAppException {
		MessageReceiver receiver = null;
		try {
			receiver = (MessageReceiver) Class.forName((String) singleConfig.get(ConfigConstant.IMPL_CLASS.getValue()))
					.getConstructor(String.class, Properties.class)
					.newInstance((String) singleConfig.get(ConfigConstant.ID.getValue()),
							(Properties) PropertiesUtils.convertMapToProperties(
									(Map<String, String>) singleConfig.get(ConfigConstant.PARAMETERS.getValue())));

		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0009", singleConfig.get(ConfigConstant.ID.getValue())));
		}
		return receiver;
	}

}
