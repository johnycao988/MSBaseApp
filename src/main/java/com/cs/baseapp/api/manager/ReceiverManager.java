/**
 * 
 */
package com.cs.baseapp.api.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.messagebroker.MessageReceiver;
import com.cs.baseapp.api.messagebroker.entity.MSMessageReceiver;
import com.cs.baseapp.api.messagebroker.pool.ObjectPool;
import com.cs.baseapp.api.messagebroker.pool.PoolObjectFactory;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.MSBaseAppStatus;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class ReceiverManager {

	private Map<String, ObjectPool<MSMessageReceiver>> pooledReceivers = new HashMap<>();

	private Logger logger = LogManager.getSystemLog();

	public ReceiverManager(List<Map<String, Object>> configs) {
		logger.info(LogManager.getServiceLogKey(), "Start to init the receiver manager!");
		try {
			if (configs == null || configs.isEmpty()) {
				throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0010"));
			}
			for (Map<String, Object> config : configs) {
				ReceiverFactory f = new ReceiverFactory(config);
				ObjectPool<MSMessageReceiver> pool = new ObjectPool<>(
						(int) config.get(ConfigConstant.POOL_SZIE.getValue()), f);
				f.setPool(pool);
				String receiverId = (String) config.get(ConfigConstant.ID.getValue());
				pooledReceivers.put(receiverId, pool);
				logger.info(LogManager.getServiceLogKey(), "Build receiver pool success! ReceiverID:" + receiverId);
			}
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0010"));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}

	public MSMessageReceiver getById(String id) throws BaseAppException {
		if (MSBaseApplication.getStatus() == MSBaseAppStatus.RUNNING.getValue()) {
			return this.pooledReceivers.get(id).getObject();
		}
		throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0046"));
	}

	public void release(MSMessageReceiver receiver) throws BaseAppException {
		this.pooledReceivers.get(receiver.getId()).releaseObject(receiver);
	}

	public void stop() {
		logger.info(LogManager.getServiceLogKey(), "Start to stop Receiver Manager!");
		Set<Entry<String, ObjectPool<MSMessageReceiver>>> entry = pooledReceivers.entrySet();
		for (Entry<String, ObjectPool<MSMessageReceiver>> e : entry) {
			try {
				e.getValue().clear();
			} catch (Exception e1) {
				BaseAppException ex = new BaseAppException(e1, LogInfoMgr.getErrorInfo("ERR_0049", e.getKey()));
				logger.write(LogManager.getServiceLogKey(), ex);
			}
		}
		this.pooledReceivers = new HashMap<>();
		logger.info(LogManager.getServiceLogKey(), "Stop Receiver Manager finish!");
	}

}

class ReceiverFactory implements PoolObjectFactory<MSMessageReceiver> {

	private Map<String, Object> config;

	private ObjectPool<MSMessageReceiver> pool;

	public ReceiverFactory(Map<String, Object> config) {
		this.config = config;
	}

	public void setPool(ObjectPool<MSMessageReceiver> pool) {
		this.pool = pool;
	}

	@Override
	public MSMessageReceiver createPoolObject() throws BaseAppException {
		boolean isPooled = (int) this.config.get(ConfigConstant.POOL_SZIE.getValue()) > 1;
		MSMessageReceiver receiver = new MSMessageReceiver(buildSingleReceiver(this.config), isPooled, this.pool);
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
					LogInfoMgr.getErrorInfo("ERR_0011", singleConfig.get(ConfigConstant.ID.getValue())));
		}
		return receiver;
	}

	@Override
	public void destroy(MSMessageReceiver receiver) throws BaseAppException {
		receiver.getReceiver().close();
	}

	@Override
	public boolean verifyReleasedObject(MSMessageReceiver t) throws BaseAppException {
		return true;
	}

}
