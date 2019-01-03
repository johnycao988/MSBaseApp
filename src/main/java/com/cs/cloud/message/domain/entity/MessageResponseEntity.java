/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.api.MessageResponseBodyService;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageResponseEntity implements MessageResponse {

	private MessageHeadBase base;
	private List<MessageResponseBodyService> bodyServices = new ArrayList<>();

	protected MessageResponseEntity(MessageHeadBase base, List<MessageResponseBodyService> bodyServices) {
		this.base = base;
		this.bodyServices = bodyServices;
	}

	@Override
	public JsonNode getJsonObject() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode jsonResponse = mapper.createObjectNode();
		jsonResponse.set(MessageConstant.MESSAGE_HEAD,
				mapper.createObjectNode().set(MessageConstant.MESSAGE_HEAD_BASE, this.base.getJsonObject()));
		ArrayNode jsonBodyServices = mapper.createArrayNode();
		for (int i = 0; i < this.bodyServices.size(); i++) {
			jsonBodyServices.add(this.bodyServices.get(i).getJsonObject());
		}
		jsonResponse.set(MessageConstant.MESSAGE_BODY, jsonBodyServices);
		return jsonResponse;
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
	public List<MessageResponseBodyService> getBodyServices() {
		return this.bodyServices;
	}

	@Override
	public MessageResponseBodyService getFirstBodyService() {
		return this.bodyServices.get(0);
	}

	@Override
	public MessageResponseBodyService getBodyService(String serviceId) {
		for (MessageResponseBodyService s : this.bodyServices) {
			if (s.getId().equals(serviceId)) {
				return s;
			}
		}
		return null;
	}

}
