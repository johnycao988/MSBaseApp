package com.cs.baseapp.api.messagebroker.pool;

import java.util.ArrayList;
import java.util.List;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.log.logs.LogInfoMgr;

public class ObjectPool<T> {

	private int poolSize = 0;

	private List<T> lockObjList;

	private int createdObjSize = 0;

	private PoolObjectFactory<T> poolObjectFactory = null;

	public ObjectPool(int poolSize, PoolObjectFactory<T> poolObjectFactory) {
		this.poolObjectFactory = poolObjectFactory;
		this.poolSize = poolSize;
		lockObjList = new ArrayList<>();
	}

	public int getPoolSize() {
		return this.poolSize;
	}

	public synchronized T getObject() throws BaseAppException {

		try {
			T rtnObj = null;
			while (true) {
				rtnObj = this.getLockObject();
				if (rtnObj != null)
					return rtnObj;
				else
					wait();
			}
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo(""));
		}
	}

	private T getLockObject() throws BaseAppException {
		try {

			if (this.poolSize <= 0)
				return this.poolObjectFactory.createPoolObject();

			if (!lockObjList.isEmpty()) {
				return this.lockObjList.remove(0);
			}

			if (this.createdObjSize < this.poolSize) {
				this.createdObjSize++;
				return this.poolObjectFactory.createPoolObject();
			}
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0029"));
		}
		return null;
	}

	public synchronized void releaseObject(T obj) {

		if (this.poolSize <= 0)
			return;
		lockObjList.add(obj);
		this.notifyAll();
	}

}
