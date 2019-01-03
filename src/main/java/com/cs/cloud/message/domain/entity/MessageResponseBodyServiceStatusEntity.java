/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import com.cs.cloud.message.api.MessageResponseBodyServiceStatus;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class MessageResponseBodyServiceStatusEntity implements MessageResponseBodyServiceStatus {

	private JsonNode status = new ObjectMapper().createObjectNode();

	protected MessageResponseBodyServiceStatusEntity(JsonNode status) {
		this.status = status;
	}

	@Override
	public JsonNode getJsonObject() {
		return this.status;
	}

	@Override
	public String getJsonString() {
		return getJsonObject().toString();
	}

	@Override
	public boolean isSuccess() {
		return this.status.path(MessageConstant.MESSAGE_BODY_SERVICE_STATUS_SUCCESS).booleanValue();
	}

	@Override
	public String getErrCode() {
		return this.status.path(MessageConstant.MESSAGE_BODY_SERVICE_ERROR_CODE).textValue();
	}

	@Override
	public String getErrMsg() {
		return this.status.path(MessageConstant.MESSAGE_BODY_SERVICE_ERROR_MESSAGE).textValue();
	}

}
