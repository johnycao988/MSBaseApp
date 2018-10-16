/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import com.cs.baseapp.api.messagebroker.MessageReceiver;
import com.cs.baseapp.api.messagebroker.Receiver;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.api.messagebroker.pool.ObjectPool;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public class MSMessageReceiver implements Receiver {

	private MessageReceiver receiver;

	private boolean isPooled;

	private ObjectPool<MSMessageReceiver> pool;

	public String getId() {
		return this.receiver.getId();
	}

	public MSMessageReceiver(MessageReceiver receiver, boolean isPooled, ObjectPool<MSMessageReceiver> pool) {
		this.receiver = receiver;
		this.isPooled = isPooled;
		this.pool = pool;
	}

	@Override
	public void initialize() throws BaseAppException {
		this.receiver.initialize();
	}

	@Override
	public MessageResponse recv(TranslationMessage msgRequest) throws BaseAppException {
		return this.receiver.recv(msgRequest);
	}

	@Override
	public void close() throws BaseAppException {
		if (this.isPooled && this.pool != null) {
			this.pool.releaseObject(this);
			return;
		}
		this.receiver.close();

	}

}
