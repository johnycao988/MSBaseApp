/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import com.cs.baseapp.api.messagebroker.pool.PoolObjectFactory;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public class MessageProcessTaskFactory implements PoolObjectFactory<MessageProcessTask> {

	@Override
	public MessageProcessTask createPoolObject() throws BaseAppException {
		return new MessageProcessTask();
	}

	@Override
	public void destroy(MessageProcessTask t) throws BaseAppException {
		t.clear();
	}

	@Override
	public boolean verifyReleasedObject(MessageProcessTask t) throws BaseAppException {
		return true;
	}

}
