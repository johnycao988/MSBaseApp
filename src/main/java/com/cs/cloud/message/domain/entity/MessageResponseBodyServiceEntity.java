/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import com.cs.cloud.message.api.MessageResponseBodyService;
import com.cs.cloud.message.api.MessageResponseBodyServiceStatus;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class MessageResponseBodyServiceEntity implements MessageResponseBodyService {

	private JsonNode bodyService = new ObjectMapper().createObjectNode();

	private MessageResponseBodyServiceStatus status;

	protected MessageResponseBodyServiceEntity(JsonNode bodyService, MessageResponseBodyServiceStatus status) {
		this.bodyService = bodyService;
		this.status = status;
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
	public String getId() {
		return this.bodyService.path(MessageConstant.MESSAGE_BODY_SERVICE_ID).textValue();
	}

	@Override
	public String getSecret() {
		return this.bodyService.path(MessageConstant.MESSAGE_BODY_SERVICE_SECRET).textValue();
	}

	@Override
	public String getToken() {
		return this.bodyService.path(MessageConstant.MESSAGE_BODY_SERVICE_TOKEN).textValue();
	}

	@Override
	public String getUserName() {
		return this.bodyService.path(MessageConstant.MESSAGE_BODY_SERVICE_USER_NAME).textValue();
	}

	@Override
	public String getAppliactionId() {
		return this.bodyService.path(MessageConstant.MESSAGE_BODY_SERVICE_APPLICATION_ID).textValue();
	}

	@Override
	public MessageResponseBodyServiceStatus getStatus() {
		return this.status;
	}

	@Override
	public JsonNode getResponseData() {
		return this.bodyService.path(MessageConstant.MESSAGE_BODY_SERVICE_RESPONSE_DATA);
	}

}
