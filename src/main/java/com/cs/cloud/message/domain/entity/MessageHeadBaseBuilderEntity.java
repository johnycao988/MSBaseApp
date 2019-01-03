/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import org.apache.commons.lang3.StringUtils;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.builder.MessageHeadBaseBuilder;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.cs.cloud.message.domain.utils.UuidUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageHeadBaseBuilderEntity implements MessageHeadBaseBuilder {

	private ObjectNode base = new ObjectMapper().createObjectNode();

	@Override
	public MessageHeadBase build() {
		if (!this.base.has(MessageConstant.MESSAGE_HEAD_BASE_ID)
				|| this.base.get(MessageConstant.MESSAGE_HEAD_BASE_ID).textValue().isEmpty()) {
			this.base.put(MessageConstant.MESSAGE_HEAD_BASE_ID, UuidUtils.get8UUID());
		}
		if (!this.base.has(MessageConstant.MESSAGE_HEAD_BASE_SEQUENCE)
				|| this.base.get(MessageConstant.MESSAGE_HEAD_BASE_SEQUENCE).textValue().isEmpty()) {
			this.base.put(MessageConstant.MESSAGE_HEAD_BASE_SEQUENCE, 1 + "/" + 1);
		}
		this.base.put(MessageConstant.MESSAGE_HEAD_BASE_TIMESTAMP, System.currentTimeMillis());
		return new MessageHeadBaseEntity(this.base);
	}

	@Override
	public MessageHeadBaseBuilder setId(String id) {
		if (!StringUtils.isEmpty(id)) {
			this.base.put(MessageConstant.MESSAGE_HEAD_BASE_ID, id);
		}
		return this;
	}

	@Override
	public MessageHeadBaseBuilder setSequence(int index, int total) {
		this.base.put(MessageConstant.MESSAGE_HEAD_BASE_SEQUENCE, index + "/" + total);
		return this;
	}

	@Override
	public MessageHeadBaseBuilder setCorrelationId(String correlationId) {
		if (!StringUtils.isEmpty(correlationId)) {
			this.base.put(MessageConstant.MESSAGE_HEAD_BASE_CORRELATION_ID, correlationId);
		}
		return this;
	}

	@Override
	public MessageHeadBaseBuilder setExpireTime(int expireTime) {
		this.base.put(MessageConstant.MESSAGE_HEAD_BASE_EXPIRE_TIME, expireTime);
		return this;
	}

	@Override
	public MessageHeadBaseBuilder setType(int type) {
		this.base.put(MessageConstant.MESSAGE_HEAD_BASE_TYPE, type);
		return this;
	}

}
