/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public class MSMessageReceiver implements Receiver {

	private MessageReceiver receiver;

	private boolean isPooled;

	public String getId() {
		return this.receiver.getId();
	}

	public MSMessageReceiver(MessageReceiver receiver, boolean isPooled) {
		this.receiver = receiver;
		this.isPooled = isPooled;
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
	public void close() {
		if (this.isPooled) {
			MSBaseApplication.getMessageBroker().releaseReceiver(this);
		}
		this.receiver.close();

	}

}
