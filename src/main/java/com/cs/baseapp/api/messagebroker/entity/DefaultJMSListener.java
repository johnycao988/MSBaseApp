/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.List;
import java.util.Properties;

import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.api.messagebroker.MessageListener;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public class DefaultJMSListener extends MessageListener {

	public DefaultJMSListener(String id, int maxProcessThreads, Properties prop, List<MessageFilter> filters) {
		super(id, maxProcessThreads, prop, filters);
	}

	@Override
	public void initialize() throws BaseAppException {

	}

	@Override
	public void start() throws BaseAppException {

	}

	@Override
	public void stop() throws BaseAppException {

	}

}
