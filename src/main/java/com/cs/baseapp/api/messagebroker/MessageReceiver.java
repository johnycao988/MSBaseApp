/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Donald.Wang
 *
 */
public abstract class MessageReceiver implements Receiver {

	protected String id;

	protected int poolSize;

	protected Properties prop;

	public MessageReceiver(String id, Properties prop) {
		this.id = id;
		this.prop = prop;
	}

	public String getId() {
		return this.id;
	}

	public String getproperty(TranslationMessage msgRequest, String key) {
		String value = msgRequest.getProperty(key);
		return StringUtils.isEmpty(value) ? this.prop.getProperty(key) : value;
	}

	public String getProperty(String key) {
		return this.prop.getProperty(key);
	}

}
