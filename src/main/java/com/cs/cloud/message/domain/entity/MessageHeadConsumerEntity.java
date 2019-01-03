/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import com.cs.cloud.message.api.MessageHeadConsumer;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class MessageHeadConsumerEntity implements MessageHeadConsumer {

	private JsonNode consumer = new ObjectMapper().createObjectNode();

	protected MessageHeadConsumerEntity(JsonNode consumer) {
		this.consumer = consumer;
	}

	@Override
	public JsonNode getJsonObject() {
		return this.consumer;
	}

	@Override
	public String getJsonString() {
		return getJsonObject().toString();
	}

	@Override
	public String getClientId() {
		return this.consumer.path(MessageConstant.MESSAGE_HEAD_CONSUMER_CLIENT_ID).textValue();
	}

	@Override
	public String getId() {
		return this.consumer.path(MessageConstant.MESSAGE_HEAD_CONSUMER_ID).textValue();
	}

	@Override
	public String getSecret() {
		return this.consumer.path(MessageConstant.MESSAGE_HEAD_CONSUMER_SECRET).textValue();
	}

	@Override
	public String getToken() {
		return this.consumer.path(MessageConstant.MESSAGE_HEAD_CONSUMER_TOKEN).textValue();
	}

	@Override
	public String getUserName() {
		return this.consumer.path(MessageConstant.MESSAGE_HEAD_CONSUMER_USER_NAME).textValue();
	}

}
