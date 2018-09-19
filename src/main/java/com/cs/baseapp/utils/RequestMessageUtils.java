/**
 * 
 */
package com.cs.baseapp.utils;

import java.util.ArrayList;
import java.util.List;

import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.builder.MessageRequestBuilder;
import com.cs.cloud.message.domain.factory.MessageFactory;

/**
 * @author Donald.Wang
 *
 */
public class RequestMessageUtils {

	private RequestMessageUtils() {

	}

	/**
	 * @param mutiOriginReqMsg
	 * @return List
	 * @desc This method is used to demerge the Multiple Request Message, and the
	 *       sub request message is use the original message head information.
	 *
	 */
	public static List<MessageRequest> demergeMultipleReqMsg(MessageRequest mutiOriginReqMsg) {
		List<MessageHeadService> headServices = mutiOriginReqMsg.getServices();
		List<MessageRequest> requests = new ArrayList<>();
		if (headServices.size() <= 1) {
			requests.add(mutiOriginReqMsg);
			return requests;
		}
		for (MessageHeadService headService : headServices) {
			MessageRequestBuilder builder = MessageFactory.getRequestMessageBuilder();
			builder.setBase(mutiOriginReqMsg.getBase());
			builder.setConsumer(mutiOriginReqMsg.getConsumer());
			builder.setTransaction(mutiOriginReqMsg.getTransaction());
			builder.addService(headService);
			builder.addBodyService(mutiOriginReqMsg.getBodyService(headService.getId()));
			requests.add(builder.build());
		}
		return requests;
	}

	/**
	 * @param mutiOriginReqMsg
	 * @param templateMsg
	 * @return List
	 * @desc The method is used to demerge the Multiple Request Message, and the sub
	 *       message is use the template message base information.
	 * 
	 */
	public static List<MessageRequest> demergeMultipleReqMsg(MessageRequest mutiOriginReqMsg,
			MessageRequest templateMsg) {
		List<MessageHeadService> headServices = mutiOriginReqMsg.getServices();
		List<MessageRequest> requests = new ArrayList<>();
		if (headServices.size() <= 1) {
			requests.add(mutiOriginReqMsg);
			return requests;
		}
		for (MessageHeadService headService : headServices) {
			MessageRequestBuilder builder = MessageFactory.getRequestMessageBuilder();
			builder.setBase(templateMsg.getBase());
			builder.setConsumer(templateMsg.getConsumer());
			builder.setTransaction(templateMsg.getTransaction());
			builder.addService(headService);
			builder.addBodyService(mutiOriginReqMsg.getBodyService(headService.getId()));
			requests.add(builder.build());
		}
		return requests;
	}

	/**
	 * @param mutiOriginReqMsg
	 * @param handle
	 * @return List
	 * @see RequestMessageDemergeHandle
	 * @desc This method is used the handler to get the base information from the
	 *       handle for different sub message.
	 */

	public static List<MessageRequest> demergeMultipleReqMsg(MessageRequest mutiOriginReqMsg,
			RequestMessageDemergeHandle handle) {
		List<MessageHeadService> headServices = mutiOriginReqMsg.getServices();
		List<MessageRequest> requests = new ArrayList<>();
		if (headServices.size() <= 1) {
			requests.add(mutiOriginReqMsg);
			return requests;
		}
		for (MessageHeadService headService : headServices) {
			MessageRequestBuilder builder = MessageFactory.getRequestMessageBuilder();
			builder.setBase(handle.getHeadBase(headService.getId()));
			builder.setConsumer(handle.getHeadConsumer(headService.getId()));
			builder.setTransaction(handle.getHeadTrx(headService.getId()));
			builder.addService(headService);
			builder.addBodyService(mutiOriginReqMsg.getBodyService(headService.getId()));
			requests.add(builder.build());
		}
		return requests;
	}

}
