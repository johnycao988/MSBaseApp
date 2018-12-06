/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.api.messagebroker.event.EventMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public class DefaultEventMessageFilter extends BaseMessageFilter {

	public DefaultEventMessageFilter(String id, Properties prop, String authRuleId) {
		super(id, prop, authRuleId);
	}

	@Override
	public void doWebFilter(MessageRequest csReqMsg, ServletRequest request, ServletResponse response)
			throws BaseAppException, MessageException {
		// do nothing
	}

	@Override
	public void doListenerFilter(MessageRequest requestMsg) throws BaseAppException {
		// do nothing
	}

	@Override
	public void doListenerEventFilter(TranslationMessage message) throws BaseAppException {
		try {
			EventMessage eventMsg = message.getInboundEventMessage();
			MSBaseApplication.getMessageBroker().getEventManager().getEventOperator().onEvent(eventMsg);
		} catch (MessageException e) {
			e.printStackTrace();
		}

	}

}
