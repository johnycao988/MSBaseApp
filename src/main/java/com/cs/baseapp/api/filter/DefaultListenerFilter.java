/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.List;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.RequestMessageUtils;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class DefaultListenerFilter extends BaseMessageFilter {

	private Logger logger = LogManager.getSystemLog();

	public DefaultListenerFilter(String id, Properties prop) {
		super(id, prop);
	}

	@Override
	public void doWebFilter(MessageRequest csReqMsg, ServletRequest request, ServletResponse response)
			throws BaseAppException, MessageException {
		// do nothing
	}

	@Override
	public void doListenerFilter(MessageRequest requestMsg) throws BaseAppException {
		List<MessageRequest> subRequests = RequestMessageUtils.demergeMultipleReqMsg(requestMsg);
		for (MessageRequest subReq : subRequests) {
			try {
				MSBaseApplication.getMessageBroker().invokeService(subReq);
			} catch (Exception e) {
				logger.write(LogManager.getServiceLogKey(),
						new BaseAppException(e, LogInfoMgr.getErrorInfo("", subReq.getJsonString())));
			}
		}

	}

}
