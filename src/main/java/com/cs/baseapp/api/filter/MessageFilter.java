/**
 * 
 */
package com.cs.baseapp.api.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public interface MessageFilter {

	public void doWebFilter(MessageRequest csReqMsg, ServletRequest request, ServletResponse response)
			throws BaseAppException, MessageException;

	public void doListenerFilter(MessageRequest requestMsg) throws BaseAppException;
}
