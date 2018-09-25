/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.Properties;

import com.cs.baseapp.api.messagebroker.ConnectionPoolManager;
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

	private ConnectionPoolManager connPool;

	public DefaultJMSSender(String id, Properties prop) {
		super(id, prop);
	}

	@Override
	public void initialize() throws BaseAppException {
		System.out.println("Init JMS Sender Success!");
	}

	@Override
	public void sendAsyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		System.out.println("Will Sender JMS Async Message!");
	}

	@Override
	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException, MessageException {
		System.out.println("Will Sender JMS Sync Message!");
		return null;
	}

	@Override
	public void close() {
		this.connPool.close();
	}

}
