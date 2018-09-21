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
public abstract class MessageSender implements Sender {

	protected String id;

	protected Properties prop;

	public MessageSender(String id, Properties prop) {
		this.id = id;
		this.prop = prop;
	}

	@Override
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

}
