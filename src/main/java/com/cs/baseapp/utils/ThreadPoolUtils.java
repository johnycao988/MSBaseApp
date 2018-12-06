/**
 * 
 */
package com.cs.baseapp.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class ThreadPoolUtils {

	private ThreadPoolUtils() {

	}

	public static ThreadPoolExecutor getBlockingThreadPool(int corePoolSize, int maximumPoolSize, int waitingQueueSize)
			throws BaseAppException {
		if (waitingQueueSize < 1) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo(""));
		}
		return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(waitingQueueSize), new BlockingHandler());
	}

}

class BlockingHandler implements RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		try {
			executor.getQueue().put(r);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
