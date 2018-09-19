/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public abstract class MessageReceiver {

	protected String id;

	protected int poolSize;

	protected Properties prop;

	public MessageReceiver(String id, int poolSize, Properties prop) {
		this.id = id;
		this.poolSize = poolSize;
		this.prop = prop;
	}

	public abstract void initialize() throws BaseAppException;

	public abstract MessageResponse recv(TranslationMessage msgRequest) throws BaseAppException;

	public String getId() {
		return this.id;
	}

	public String getproperty(TranslationMessage msgRequest, String key) {
		String value = msgRequest.getProperty(key);
		return StringUtils.isEmpty(value) ? this.prop.getProperty(key) : value;
	}

	public int getPoolSize() {
		return this.poolSize;
	}

}
