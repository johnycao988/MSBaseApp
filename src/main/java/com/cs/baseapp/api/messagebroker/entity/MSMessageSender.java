/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.messagebroker.Sender;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.api.messagebroker.pool.ObjectPool;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public class MSMessageSender implements Sender {

	private MessageSender sender;

	private boolean isPooled;

	private ObjectPool<MSMessageSender> pool;

	public MSMessageSender(MessageSender sender, boolean isPooled, ObjectPool<MSMessageSender> pool) {
		this.sender = sender;
		this.isPooled = isPooled;
		this.pool = pool;
	}

	@Override
	public void initialize() throws BaseAppException {
		this.sender.initialize();
	}

	@Override
	public void sendAsyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		this.sender.sendAsyncMessage(requestMsg);
	}

	@Override
	public void close() throws BaseAppException {
		if (this.isPooled) {
			this.pool.releaseObject(this);
			return;
		}
		this.sender.close();

	}

	@Override
	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		return this.sender.sendSyncMessage(requestMsg);
	}

	@Override
	public String getId() {
		return this.sender.getId();
	}

}
