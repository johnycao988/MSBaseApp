/**
 * 
 */
package com.cs.baseapp.api.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.cs.baseapp.api.filter.FilterFactory;
import com.cs.baseapp.api.messagebroker.BaseMessageListener;
import com.cs.baseapp.api.messagebroker.entity.MessageProcessTask;
import com.cs.baseapp.api.messagebroker.entity.MessageProcessTaskFactory;
import com.cs.baseapp.api.messagebroker.listener.MSBaseAppListenerThread;
import com.cs.baseapp.api.messagebroker.pool.ObjectPool;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.baseapp.utils.ThreadPoolUtils;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class ListenerManager {

	private Logger logger = LogManager.getSystemLog();

	private static Map<String, ExecutorService> threadPools = new HashMap<>();

	private static Map<String, ObjectPool<MessageProcessTask>> messageProcessTaskPool = new HashMap<>();

	private static Map<String, List<MSBaseAppListenerThread>> listeners = new HashMap<>();

	public ListenerManager(List<Map<String, Object>> listenersConfig) {
		try {
			if (listenersConfig == null || listenersConfig.isEmpty()) {
				throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0054"));
			}
			for (Map<String, Object> singleConfig : listenersConfig) {
				List<MSBaseAppListenerThread> listener = buildSingleListener(singleConfig);
				listeners.put(listener.get(0).getListener().getId(), listener);
				threadPools.put(listener.get(0).getListener().getId(), ThreadPoolUtils.getBlockingThreadPool(5,
						listener.get(0).getListener().getMaxProcessThreads(), 1));
				messageProcessTaskPool.put(listener.get(0).getListener().getId(), new ObjectPool<MessageProcessTask>(
						listener.get(0).getListener().getMaxProcessThreads(), new MessageProcessTaskFactory()));
			}
		} catch (Exception e) {
			logger.write(LogManager.getServiceLogKey(), new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0053")));
		}
	}

	public static void doMessage(String listenerId, Runnable task) {
		threadPools.get(listenerId).execute(task);
	}

	public static MessageProcessTask getMessageTaskObject(String listenerId) throws BaseAppException {
		return messageProcessTaskPool.get(listenerId).getObject();
	}

	public static void releaseMessageTaskObject(String listenerId, MessageProcessTask taskObj) throws BaseAppException {
		messageProcessTaskPool.get(listenerId).releaseObject(taskObj);
	}

	public BaseMessageListener getById(String id) {
		return listeners.get(id).get(0).getListener();
	}

	public void start() {
		Set<Entry<String, List<MSBaseAppListenerThread>>> set = listeners.entrySet();
		for (Entry<String, List<MSBaseAppListenerThread>> entry : set) {
			List<MSBaseAppListenerThread> listeners = entry.getValue();
			for (MSBaseAppListenerThread listener : listeners) {
				try {
					listener.start();
				} catch (BaseAppException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stop(String id) throws BaseAppException {
		List<MSBaseAppListenerThread> listenerList = listeners.get(id);
		for (MSBaseAppListenerThread listener : listenerList) {
			listener.stop();
		}
		ExecutorService pool = threadPools.get(id);
		pool.shutdown();
		try {
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow();
				if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
					throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0047"));
				}
			}
		} catch (InterruptedException e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0048"));
			Thread.currentThread().interrupt();
			logger.write(LogManager.getServiceLogKey(), ex);
		}
		listenerList.clear();
		listeners.remove(id);
		threadPools.remove(id);
	}

	public void stopAll() throws BaseAppException {
		logger.info(LogManager.getServiceLogKey(), "Start to stop Listener Manager!");
		Set<String> keys = listeners.keySet();
		for (String key : keys) {
			stop(key);
		}
		listeners.clear();
		logger.info(LogManager.getServiceLogKey(), "Stop Listener Manager finish!");
	}

	@SuppressWarnings("unchecked")
	private List<MSBaseAppListenerThread> buildSingleListener(Map<String, Object> singleConfig) {
		List<MSBaseAppListenerThread> liteners = new ArrayList<>();
		if (singleConfig == null || singleConfig.isEmpty()) {
			return liteners;
		}
		String implClass = (String) singleConfig.get(ConfigConstant.IMPL_CLASS.getValue());
		String listenerId = (String) singleConfig.get(ConfigConstant.ID.getValue());
		int connections = (int) singleConfig.get(ConfigConstant.CONNECTIONS.getValue());
		try {
			for (int i = 0; i < connections; i++) {
				BaseMessageListener listener = (BaseMessageListener) Class.forName(implClass)
						.getConstructor(String.class, int.class, Properties.class, List.class, int.class, String.class)
						.newInstance(listenerId, (int) singleConfig.get(ConfigConstant.MAX_PROCESS_THREADS.getValue()),
								(Properties) PropertiesUtils.convertMapToProperties(
										(Map<String, String>) singleConfig.get(ConfigConstant.PARAMETERS.getValue())),
								FilterFactory.buildListenerFilters((List<Map<String, Object>>) singleConfig
										.get(ConfigConstant.MESSAGE_FILTER.getValue())),
								connections, (String) singleConfig.get(ConfigConstant.TRANS_CLASS.getValue()));
				MSBaseAppListenerThread t = new MSBaseAppListenerThread(listener);
//				t.start();
				liteners.add(t);
			}

		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0016", listenerId));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
		return liteners;
	}

}
