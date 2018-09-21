/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.Properties;

import com.cs.baseapp.api.messagebroker.MessageReceiver;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public class DefaultJMSReceiver extends MessageReceiver {

	public DefaultJMSReceiver(String id, Properties prop) {
		super(id, prop);
	}

	@Override
	public void initialize() throws BaseAppException {

	}

	@Override
	public MessageResponse recv(TranslationMessage msgRequest) throws BaseAppException {
		return null;
	}

	@Override
	public void close() {

	}

}
