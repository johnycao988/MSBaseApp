/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.Properties;

import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public class DefaultJMSSender extends MessageSender {

	public DefaultJMSSender(String id, int poolSize, Properties prop) {
		super(id, poolSize, prop);
	}

	@Override
	public void initialize() throws BaseAppException {

	}

	@Override
	public void sendAsyncMessage(TranslationMessage requestMsg) throws BaseAppException {

	}

	@Override
	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException, MessageException {
		return null;
	}

}
