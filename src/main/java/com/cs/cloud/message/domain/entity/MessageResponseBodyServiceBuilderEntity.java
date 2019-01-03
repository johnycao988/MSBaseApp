/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import org.apache.commons.lang3.StringUtils;

import com.cs.cloud.message.api.MessageResponseBodyService;
import com.cs.cloud.message.api.MessageResponseBodyServiceStatus;
import com.cs.cloud.message.api.builder.MessageResponseBodyServiceBuilder;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageResponseBodyServiceBuilderEntity implements MessageResponseBodyServiceBuilder {

	private ObjectNode bodyService = new ObjectMapper().createObjectNode();
	private MessageResponseBodyServiceStatus status;

	@Override
	public MessageResponseBodyService build() {
		return new MessageResponseBodyServiceEntity(this.bodyService, this.status);
	}

	@Override
	public MessageResponseBodyServiceBuilder setApplicationId(String applicationId) {
		if (!StringUtils.isEmpty(applicationId)) {
			this.bodyService.put(MessageConstant.MESSAGE_BODY_SERVICE_APPLICATION_ID, applicationId);
		}
		return this;
	}

	@Override
	public MessageResponseBodyServiceBuilder setId(String id) {
		if (!StringUtils.isEmpty(id)) {
			this.bodyService.put(MessageConstant.MESSAGE_BODY_SERVICE_ID, id);
		}
		return this;
	}

	@Override
	public MessageResponseBodyServiceBuilder setSecret(String secret) {
		if (!StringUtils.isEmpty(secret)) {
			this.bodyService.put(MessageConstant.MESSAGE_BODY_SERVICE_SECRET, secret);
		}
		return this;
	}

	@Override
	public MessageResponseBodyServiceBuilder setToken(String token) {
		if (!StringUtils.isEmpty(token)) {
			this.bodyService.put(MessageConstant.MESSAGE_BODY_SERVICE_TOKEN, token);
		}
		return this;
	}

	@Override
	public MessageResponseBodyServiceBuilder setUserName(String userName) {
		if (!StringUtils.isEmpty(userName)) {
			this.bodyService.put(MessageConstant.MESSAGE_BODY_SERVICE_USER_NAME, userName);
		}
		return this;
	}

	@Override
	public MessageResponseBodyServiceBuilder setServiceStatus(MessageResponseBodyServiceStatus status) {
		this.status = status;
		this.bodyService.set(MessageConstant.MESSAGE_BODY_SERVICE_STATUS, status.getJsonObject());
		return this;
	}

	@Override
	public MessageResponseBodyServiceBuilder setResponseData(Object resData) throws MessageException {
		if (resData == null) {
			return this;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(resData);
			this.bodyService.set(MessageConstant.MESSAGE_BODY_SERVICE_RESPONSE_DATA, mapper.readTree(json));
		} catch (Exception e) {
			throw new MessageException("Invalid response data", e);
		}
		return this;
	}

}
