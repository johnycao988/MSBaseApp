/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.Properties;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public abstract class BusinessService {

	private MessageRequest req;

	private Properties prop;

	public BusinessService(MessageRequest req, Properties prop) {
		this.req = req;
		this.prop = prop;
	}

	public abstract MessageResponse process() throws BaseAppException, MessageException;

	public MessageRequest getReq() {
		return req;
	}

	public Properties getProp() {
		return prop;
	}

}
