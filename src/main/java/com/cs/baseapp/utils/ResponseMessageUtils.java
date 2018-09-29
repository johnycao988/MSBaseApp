/**
 * 
 */
package com.cs.baseapp.utils;

import java.util.List;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.api.builder.MessageResponseBuilder;
import com.cs.cloud.message.domain.factory.MessageFactory;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class ResponseMessageUtils {

	private ResponseMessageUtils() {

	}

	public static MessageResponse mergeResponse(MessageRequest originReqMsg, List<MessageResponse> responses)
			throws BaseAppException {
		if (originReqMsg == null || responses == null) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0006"));
		}
		MessageResponseBuilder builder = MessageFactory.getResponseMessageBuilder();
		builder.setBase(builder.getMessageHeadBaseBuilder().setCorrelationId(originReqMsg.getBase().getId())
				.setType(MessageConstant.MESSAGE_HEAD_BASE_TYPE_RESPONSE).build());
		for (MessageResponse r : responses) {
			if (r == null) {
				continue;
			}
			builder.addResponseBodyService(r.getFirstBodyService());
		}
		return builder.build();
	}

}
