/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public class DefaultListenerFilter extends BaseMessageFilter {

	public DefaultListenerFilter(String id, Properties prop, int sequence) {
		super(id, prop, sequence);
	}

	@Override
	public void doWebFilter(MessageRequest csReqMsg, ServletRequest request, ServletResponse response)
			throws BaseAppException, MessageException {

	}

	@Override
	public void doListenerFilter(MessageRequest requestMsg) throws BaseAppException {

	}

}
