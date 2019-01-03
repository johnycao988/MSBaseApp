/**
 * 
 */
package com.cs.cloud.message.domain.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.MessageHeadConsumer;
import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.api.MessageHeadTransaction;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageRequestBodyService;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.api.MessageResponseBodyService;
import com.cs.cloud.message.api.MessageResponseBodyServiceStatus;
import com.cs.cloud.message.api.builder.MessageHeadBaseBuilder;
import com.cs.cloud.message.api.builder.MessageHeadConsumerBuilder;
import com.cs.cloud.message.api.builder.MessageHeadServiceBuilder;
import com.cs.cloud.message.api.builder.MessageHeadTransactionBuilder;
import com.cs.cloud.message.api.builder.MessageRequestBodyServiceBuilder;
import com.cs.cloud.message.api.builder.MessageRequestBuilder;
import com.cs.cloud.message.api.builder.MessageResponseBodyServiceBuilder;
import com.cs.cloud.message.api.builder.MessageResponseBodyServiceStatusBuilder;
import com.cs.cloud.message.api.builder.MessageResponseBuilder;
import com.cs.cloud.message.domain.entity.MessageRequestBuilderEntity;
import com.cs.cloud.message.domain.entity.MessageResponseBuilderEntity;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class MessageFactory {

	private MessageFactory() {

	}

	public static MessageRequest getRequestMessage(String jsonMessage) throws MessageException {
		JsonNode jsonRequestMsg = getJosnObject(jsonMessage);
		if (StringUtils.isEmpty(jsonMessage)
				|| MessageConstant.MESSAGE_HEAD_BASE_TYPE_REQUEST != getJsonMessageType(jsonRequestMsg)) {
			throw new MessageException("invalid json message!");
		}
		JsonNode jsonHead = jsonRequestMsg.path(MessageConstant.MESSAGE_HEAD);
		MessageRequestBuilder requestBuilder = MessageFactory.getRequestMessageBuilder();
		return requestBuilder
				.setBase(convertJsonToMessageHeadbase(jsonHead.path(MessageConstant.MESSAGE_HEAD_BASE),
						requestBuilder.getBaseBuilder(), MessageConstant.MESSAGE_HEAD_BASE_TYPE_REQUEST))
				.setConsumer(convertJsonToMessageHeadConsumer(jsonHead.path(MessageConstant.MESSAGE_HEAD_CONSUMER),
						requestBuilder.getConsumerBuilder()))
				.setServices(convertJsonToMessageHeadServices(jsonHead.path(MessageConstant.MESSAGE_HEAD_SERVICES),
						requestBuilder))
				.setTransaction(
						convertJsonToMessageHeadTransaction(jsonHead.path(MessageConstant.MESSAGE_HEAD_TRANSACTION),
								requestBuilder.getTransactionBuilder()))
				.setBodyServices(convertJsonToRequestBodyServices(jsonRequestMsg.path(MessageConstant.MESSAGE_BODY),
						requestBuilder))
				.build();
	}

	public static MessageResponse getResopnseMessage(String jsonMessage) throws MessageException {
		JsonNode jsonResponseMsg = getJosnObject(jsonMessage);
		if (StringUtils.isEmpty(jsonMessage)
				|| MessageConstant.MESSAGE_HEAD_BASE_TYPE_RESPONSE != getJsonMessageType(jsonResponseMsg)) {
			throw new MessageException("invalid json message!");
		}
		JsonNode jsonHead = jsonResponseMsg.path(MessageConstant.MESSAGE_HEAD);
		MessageResponseBuilder responseBuilder = MessageFactory.getResponseMessageBuilder();
		return responseBuilder
				.setBase(convertJsonToMessageHeadbase(jsonHead.path(MessageConstant.MESSAGE_HEAD_BASE),
						responseBuilder.getMessageHeadBaseBuilder(), MessageConstant.MESSAGE_HEAD_BASE_TYPE_RESPONSE))
				.setResponseBodyServices(convertJsonToResponseBodyServices(
						jsonResponseMsg.path(MessageConstant.MESSAGE_BODY), responseBuilder))
				.build();

	}

	public static MessageRequestBuilder getRequestMessageBuilder() {
		return new MessageRequestBuilderEntity();
	}

	public static MessageResponseBuilder getResponseMessageBuilder() {
		return new MessageResponseBuilderEntity();
	}

	private static JsonNode getJosnObject(String jsonMessage) throws MessageException {
		JsonNode jsonObj = null;
		try {
			jsonObj = new ObjectMapper().readTree(jsonMessage);
		} catch (IOException e) {
			throw new MessageException("The JsonMessage is invalid!", e);
		}
		return jsonObj;
	}

	private static int getJsonMessageType(JsonNode jsonMessage) {
		return jsonMessage.path(MessageConstant.MESSAGE_HEAD).get(MessageConstant.MESSAGE_HEAD_BASE)
				.path(MessageConstant.MESSAGE_HEAD_BASE_TYPE).intValue();
	}

	private static MessageHeadBase convertJsonToMessageHeadbase(JsonNode jsonMessage, MessageHeadBaseBuilder builder,
			int messageType) {
		String[] sequence = jsonMessage.get(MessageConstant.MESSAGE_HEAD_BASE_SEQUENCE).textValue().split("/");
		int index = Integer.parseInt(sequence[0]);
		int total = Integer.parseInt(sequence[1]);
		builder.setId(jsonMessage.path(MessageConstant.MESSAGE_HEAD_BASE_ID).textValue())
				.setType(jsonMessage.path(MessageConstant.MESSAGE_HEAD_BASE_TYPE).intValue()).setSequence(total, index);
		if (MessageConstant.MESSAGE_HEAD_BASE_TYPE_REQUEST == messageType) {
			builder.setExpireTime(jsonMessage.path(MessageConstant.MESSAGE_HEAD_BASE_EXPIRE_TIME).intValue());
		} else {
			builder.setCorrelationId(jsonMessage.path(MessageConstant.MESSAGE_HEAD_BASE_CORRELATION_ID).textValue());

		}
		return builder.build();
	}

	private static MessageHeadConsumer convertJsonToMessageHeadConsumer(JsonNode jsonMessage,
			MessageHeadConsumerBuilder builder) {
		return builder.setClientId(jsonMessage.path(MessageConstant.MESSAGE_HEAD_CONSUMER_CLIENT_ID).textValue())
				.setId(jsonMessage.path(MessageConstant.MESSAGE_HEAD_CONSUMER_ID).textValue())
				.setSecret(jsonMessage.path(MessageConstant.MESSAGE_HEAD_CONSUMER_SECRET).textValue())
				.setToken(jsonMessage.path(MessageConstant.MESSAGE_HEAD_CONSUMER_TOKEN).textValue())
				.setUserName(jsonMessage.path(MessageConstant.MESSAGE_HEAD_CONSUMER_USER_NAME).textValue()).build();
	}

	private static List<MessageHeadService> convertJsonToMessageHeadServices(JsonNode jsonMessage,
			MessageRequestBuilder requestBuilder) {
		MessageHeadServiceBuilder builder = requestBuilder.getServiceBuilder();
		List<MessageHeadService> services = new ArrayList<>();
		for (int i = 0; i < jsonMessage.size(); i++) {
			JsonNode jsonSingleService = jsonMessage.get(i);
			builder.setId(jsonSingleService.path(MessageConstant.MESSAGE_HEAD_SERVICE_ID).textValue())
					.setApplicationId(
							jsonSingleService.path(MessageConstant.MESSAGE_HEAD_SERVICE_APPLICATION_ID).textValue())
					.setSync(jsonSingleService.path(MessageConstant.MESSAGE_HEAD_SERVICE_SYNC).booleanValue());
			JsonNode jsonProperties = jsonSingleService.path(MessageConstant.MESSAGE_HEAD_SERVICE_PROPERTIES);
			Entry<String, JsonNode> entry;
			Iterator<Entry<String, JsonNode>> propertiesIterator = jsonProperties.fields();
			while (propertiesIterator.hasNext()) {
				entry = propertiesIterator.next();
				builder.setProperty(entry.getKey(), entry.getValue().textValue());
			}
			services.add(builder.build());
			builder = requestBuilder.getServiceBuilder();
		}
		return services;
	}

	private static MessageHeadTransaction convertJsonToMessageHeadTransaction(JsonNode jsonMessage,
			MessageHeadTransactionBuilder builder) {
		return builder.setTransactionNo(jsonMessage.path(MessageConstant.MESSAGE_HEAD_TRANSACTION_TRX_NO).textValue())
				.setReferenceNo(jsonMessage.path(MessageConstant.MESSAGE_HEAD_TRANSACTION_REFERENCE_NO).textValue())
				.setUnitCode(jsonMessage.path(MessageConstant.MESSAGE_HEAD_TRANSACTION_UNIC_CODE).textValue())
				.setFunctionId(jsonMessage.path(MessageConstant.MESSAGE_HEAD_TRANSACTION_FUNCTION_ID).textValue())
				.build();
	}

	private static List<MessageRequestBodyService> convertJsonToRequestBodyServices(JsonNode jsonMessage,
			MessageRequestBuilder requestBuilder) throws MessageException {
		MessageRequestBodyServiceBuilder builder = requestBuilder.getBodyServiceBuilder();
		List<MessageRequestBodyService> services = new ArrayList<>();
		for (int i = 0; i < jsonMessage.size(); i++) {
			JsonNode jsonSingleService = jsonMessage.get(i);
			services.add(builder
					.setServiceId(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_ID).textValue())
					.setRequestData(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_REQUEST_DATA)).build());
			builder = requestBuilder.getBodyServiceBuilder();
		}
		return services;
	}

	private static List<MessageResponseBodyService> convertJsonToResponseBodyServices(JsonNode jsonMessage,
			MessageResponseBuilder respBuilder) throws MessageException {
		MessageResponseBodyServiceBuilder builder = respBuilder.getMessageResponseBodyServiceBuilder();
		MessageResponseBodyServiceStatusBuilder statusBuilder = respBuilder
				.getMessageResponseBodyServiceStatusBuilder();
		List<MessageResponseBodyService> services = new ArrayList<>();
		for (int i = 0; i < jsonMessage.size(); i++) {
			JsonNode jsonSingleService = jsonMessage.get(i);
			MessageResponseBodyServiceStatus status = statusBuilder
					.setSuccess(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_STATUS)
							.path(MessageConstant.MESSAGE_BODY_SERVICE_STATUS_SUCCESS).booleanValue())
					.setErrCode(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_STATUS)
							.path(MessageConstant.MESSAGE_BODY_SERVICE_ERROR_CODE).textValue())
					.setErrMsg(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_STATUS)
							.path(MessageConstant.MESSAGE_BODY_SERVICE_ERROR_MESSAGE).textValue())
					.build();
			services.add(builder
					.setApplicationId(
							jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_APPLICATION_ID).textValue())
					.setId(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_ID).textValue())
					.setSecret(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_SECRET).textValue())
					.setToken(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_TOKEN).textValue())
					.setUserName(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_USER_NAME).textValue())
					.setServiceStatus(status)
					.setResponseData(jsonSingleService.path(MessageConstant.MESSAGE_BODY_SERVICE_RESPONSE_DATA))
					.build());
			builder = respBuilder.getMessageResponseBodyServiceBuilder();
			statusBuilder = respBuilder.getMessageResponseBodyServiceStatusBuilder();
		}
		return services;

	}
}
