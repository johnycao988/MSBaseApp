/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.Properties;

import javax.servlet.FilterChain;
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

	public DefaultListenerFilter(Properties prop) {
		super(prop);
	}

	@Override
	public void doWebFilter(MessageRequest csReqMsg, ServletRequest request, ServletResponse response,
			FilterChain chain) throws BaseAppException, MessageException {

	}

	@Override
	public void doListenerFilter(MessageRequest requestMsg) throws BaseAppException {

	}

}
