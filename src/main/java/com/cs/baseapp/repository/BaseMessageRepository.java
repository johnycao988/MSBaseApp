/**
 * 
 */
package com.cs.baseapp.repository;

import java.util.Properties;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public abstract class BaseMessageRepository {

	private Properties prop;

	public BaseMessageRepository(Properties prop) {

		this.prop = prop;
	}

	public abstract void init() throws BaseAppException;

	public abstract void storeRequestMessage(MessageRequest req) throws BaseAppException;

	public abstract void storeResponseMessage(MessageRequest req, MessageResponse res) throws BaseAppException;

	public Properties getProperties() {
		return this.prop;
	}

	public String getProptry(String key) {
		return this.prop.getProperty(key);
	}

}
