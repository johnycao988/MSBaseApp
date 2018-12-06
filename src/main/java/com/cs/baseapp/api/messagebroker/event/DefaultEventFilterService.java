/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

import java.util.Properties;

import com.cs.baseapp.api.messagebroker.BusinessService;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public class DefaultEventFilterService extends BusinessService {

	public DefaultEventFilterService(MessageRequest req, Properties prop) {
		super(req, prop);
	}

	@Override
	public MessageResponse process() throws BaseAppException, MessageException {
		return null;
	}

}
