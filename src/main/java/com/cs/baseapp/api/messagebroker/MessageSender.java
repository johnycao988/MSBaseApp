/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public abstract class MessageSender {

	protected String id;

	protected int poolSize;

	protected Properties prop;

	public MessageSender(String id, int poolSize, Properties prop) {
		this.id = id;
		this.poolSize = poolSize;
		this.prop = prop;
	}

	public abstract void initialize() throws BaseAppException;

	public abstract void sendAsyncMessage(TranslationMessage requestMsg) throws BaseAppException;

	public abstract MessageResponse sendSyncMessage(TranslationMessage requestMsg)
			throws BaseAppException, MessageException;

	public String getId() {
		return this.id;
	}

	public String getProperty(TranslationMessage reqMsg, String key) {
		String value = reqMsg.getProperty(key);
		return StringUtils.isEmpty(value) ? this.prop.getProperty(key) : value;
	}

	public String getProperty(String key) {
		return this.prop.getProperty(key);
	}

	public int getPoolSize() {
		return this.poolSize;
	}

}
