/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.utils.RequestMessageUtils;
import com.cs.baseapp.utils.ResponseMessageUtils;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class DefaultMsgProcWebFilter extends BaseMessageFilter {

	public DefaultMsgProcWebFilter(String id, String urlPattern, Properties prop) {
		super(id, urlPattern, prop);
	}

	@Override
	public void doWebFilter(MessageRequest csReqMsg, ServletRequest request, ServletResponse response)
			throws BaseAppException {
		List<MessageResponse> responses = new ArrayList<>();
		List<MessageRequest> subRequests = RequestMessageUtils.demergeMultipleReqMsg(csReqMsg);
		for (MessageRequest r : subRequests) {
			responses.add(MSBaseApplication.getMessageBroker().invokeService(r));
		}
		try {
			response.setContentType("application/json");
			response.getOutputStream()
					.write(ResponseMessageUtils.mergeResponse(csReqMsg, responses).getJsonString().getBytes());
			response.flushBuffer();
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0005", csReqMsg.getJsonString()));
		}
	}

	@Override
	public void doListenerFilter(MessageRequest requestMsg) throws BaseAppException {
		// do nothing
	}

}
