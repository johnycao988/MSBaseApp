/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.MessageHeadConsumer;
import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.api.MessageHeadTransaction;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageRequestBodyService;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageRequestEntity implements MessageRequest {

	private MessageHeadBase base;

	private MessageHeadConsumer consumer;

	private List<MessageHeadService> headServices = new ArrayList<>();

	private MessageHeadTransaction trx;

	private List<MessageRequestBodyService> bodyServices = new ArrayList<>();

	protected MessageRequestEntity(MessageHeadBase base, MessageHeadConsumer consumer,
			List<MessageHeadService> headServices, MessageHeadTransaction trx,
			List<MessageRequestBodyService> bodyServices) {
		this.base = base;
		this.consumer = consumer;
		this.headServices = headServices;
		this.trx = trx;
		this.bodyServices = bodyServices;
	}

	@Override
	public JsonNode getJsonObject() {
		ObjectMapper objMapper = new ObjectMapper();
		ObjectNode jsonRequest = objMapper.createObjectNode();
		ObjectNode jsonHeader = objMapper.createObjectNode();
		jsonHeader.set(MessageConstant.MESSAGE_HEAD_BASE, this.base.getJsonObject());
		jsonHeader.set(MessageConstant.MESSAGE_HEAD_CONSUMER, this.consumer.getJsonObject());
		ArrayNode jsonServices = objMapper.createArrayNode();
		for (MessageHeadService s : this.headServices) {
			jsonServices.add(s.getJsonObject());
		}
		jsonHeader.set(MessageConstant.MESSAGE_HEAD_SERVICES, jsonServices);
		jsonHeader.set(MessageConstant.MESSAGE_HEAD_TRANSACTION, this.trx.getJsonObject());
		jsonRequest.set(MessageConstant.MESSAGE_HEAD, jsonHeader);
		ArrayNode jsonRequestBodys = new ObjectMapper().createArrayNode();
		for (MessageRequestBodyService b : this.bodyServices) {
			jsonRequestBodys.add(b.getJsonObject());
		}
		jsonRequest.set(MessageConstant.MESSAGE_BODY, jsonRequestBodys);
		return jsonRequest;
	}

	@Override
	public String getJsonString() {
		return getJsonObject().toString();
	}

	@Override
	public MessageHeadBase getBase() {
		return this.base;
	}

	@Override
	public MessageHeadConsumer getConsumer() {
		return this.consumer;
	}

	@Override
	public List<MessageHeadService> getServices() {
		return this.headServices;
	}

	@Override
	public MessageHeadTransaction getTransaction() {
		return this.trx;
	}

	@Override
	public MessageRequestBodyService getBodyService(String serviceId) {
		for (MessageRequestBodyService bodyService : this.bodyServices) {
			if (bodyService.getServiceId().equals(serviceId)) {
				return bodyService;
			}
		}
		return null;
	}

	@Override
	public List<MessageRequestBodyService> getBodyServices() {
		return this.bodyServices;
	}

	@Override
	public MessageRequestBodyService getFirstBodyService() {
		return this.bodyServices.get(0);
	}

}
