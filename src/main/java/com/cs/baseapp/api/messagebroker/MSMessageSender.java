/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;

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
	public void close() {
		if (this.isPooled) {
			MSBaseApplication.getMessageBroker().releaseSender(this);
		}
		this.sender.close();

	}

	@Override
	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException, MessageException {
		return this.sender.sendSyncMessage(requestMsg);
	}

	@Override
	public String getId() {
		return this.sender.getId();
	}

}
