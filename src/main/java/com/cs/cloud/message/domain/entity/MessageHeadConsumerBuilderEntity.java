/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import org.apache.commons.lang3.StringUtils;

import com.cs.cloud.message.api.MessageHeadConsumer;
import com.cs.cloud.message.api.builder.MessageHeadConsumerBuilder;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageHeadConsumerBuilderEntity implements MessageHeadConsumerBuilder {

	private ObjectNode consumer = new ObjectMapper().createObjectNode();

	@Override
	public MessageHeadConsumer build() {
		return new MessageHeadConsumerEntity(consumer);
	}

	@Override
	public MessageHeadConsumerBuilderEntity setClientId(String clientId) {
		if (!StringUtils.isEmpty(clientId)) {
			this.consumer.put(MessageConstant.MESSAGE_HEAD_CONSUMER_CLIENT_ID, clientId);
		}
		return this;
	}

	@Override
	public MessageHeadConsumerBuilder setId(String id) {
		if (!StringUtils.isEmpty(id)) {
			this.consumer.put(MessageConstant.MESSAGE_HEAD_CONSUMER_ID, id);
		}
		return this;
	}

	@Override
	public MessageHeadConsumerBuilder setSecret(String secret) {
		if (!StringUtils.isEmpty(secret)) {
			this.consumer.put(MessageConstant.MESSAGE_HEAD_CONSUMER_SECRET, secret);
		}
		return this;
	}

	@Override
	public MessageHeadConsumerBuilder setToken(String token) {
		if (!StringUtils.isEmpty(token)) {
			this.consumer.put(MessageConstant.MESSAGE_HEAD_CONSUMER_TOKEN, token);
		}
		return this;
	}

	@Override
	public MessageHeadConsumerBuilder setUserName(String userName) {
		if (!StringUtils.isEmpty(userName)) {
			this.consumer.put(MessageConstant.MESSAGE_HEAD_CONSUMER_USER_NAME, userName);
		}
		return this;
	}

}
