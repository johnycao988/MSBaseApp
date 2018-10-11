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
public class DefaultMessageRepository extends BaseMessageRepository {

	public DefaultMessageRepository(Properties prop) {
		super(prop);
	}

	@Override
	public void init() throws BaseAppException {

	}

	@Override
	public void storeMessage(MessageRequest req) throws BaseAppException {

	}

	@Override
	public void storeMessage(MessageResponse res) throws BaseAppException {

	}

}
