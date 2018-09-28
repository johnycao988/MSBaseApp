/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.messagebroker.Sender;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public class MSMessageSender implements Sender {

	private MessageSender sender;

	private boolean isPooled;

	public MSMessageSender(MessageSender sender, boolean isPooled) {
		this.sender = sender;
		this.isPooled = isPooled;

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
			MSBaseApplication.getMessageBroker().releaseSender(this);
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
