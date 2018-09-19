/**
 * 
 */
package com.cs.baseapp.utils;

import java.util.List;

import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.api.builder.MessageResponseBuilder;
import com.cs.cloud.message.domain.factory.MessageFactory;
import com.cs.cloud.message.domain.utils.MessageConstant;

/**
 * @author Donald.Wang
 *
 */
public class ResponseMessageUtils {

	private ResponseMessageUtils() {

	}

	public static MessageResponse mergeResponse(MessageRequest originReqMsg, List<MessageResponse> responses) {
		MessageResponseBuilder builder = MessageFactory.getResponseMessageBuilder();
		builder.setBase(builder.getMessageHeadBaseBuilder().setCorrelationId(originReqMsg.getBase().getId())
				.setType(MessageConstant.MESSAGE_HEAD_BASE_TYPE_RESPONSE).build());
		for (MessageResponse r : responses) {
			builder.addResponseBodyService(r.getFirstBodyService());
		}
		return builder.build();
	}

}
