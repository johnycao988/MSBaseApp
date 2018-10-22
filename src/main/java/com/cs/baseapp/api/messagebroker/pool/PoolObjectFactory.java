package com.cs.baseapp.api.messagebroker.pool;

import com.cs.baseapp.errorhandling.BaseAppException;

public interface PoolObjectFactory<T> {

	public T createPoolObject() throws BaseAppException;

	public void destroy(T t) throws BaseAppException;

	public boolean verifyReleasedObject(T t) throws BaseAppException;

}
