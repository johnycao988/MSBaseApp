/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import com.cs.cloud.message.api.MessageRequestBodyService;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class MessageRequestBodyServiceEntity implements MessageRequestBodyService {

	JsonNode bodyService = new ObjectMapper().createObjectNode();

	protected MessageRequestBodyServiceEntity(JsonNode bodyService) {
		this.bodyService = bodyService;
	}

	@Override
	public JsonNode getJsonObject() {
		return this.bodyService;
	}

	@Override
	public String getJsonString() {
		return getJsonObject().toString();
	}

	@Override
	public String getServiceId() {
		return this.bodyService.get(MessageConstant.MESSAGE_BODY_SERVICE_ID).textValue();
	}

	@Override
	public JsonNode getRequestData() {
		return this.bodyService.get(MessageConstant.MESSAGE_BODY_SERVICE_REQUEST_DATA);
	}

}
