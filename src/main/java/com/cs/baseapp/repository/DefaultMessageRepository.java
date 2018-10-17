/**
 * 
 */
package com.cs.baseapp.repository;

import java.util.Properties;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.repository.dao.MessageRepositoryDao;
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
		// do nothing
	}

	@Override
	public void storeRequestMessage(MessageRequest req) throws BaseAppException {
		MessageRepositoryDao dao = new MessageRepositoryDao();
		dao.storeMessage(req, null);
	}

	@Override
	public void storeResponseMessage(MessageRequest req, MessageResponse res) throws BaseAppException {
		MessageRepositoryDao dao = new MessageRepositoryDao();
		dao.storeMessage(req, res);
	}

}
