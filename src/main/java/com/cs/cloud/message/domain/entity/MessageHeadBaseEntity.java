/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class MessageHeadBaseEntity implements MessageHeadBase {

	JsonNode base = new ObjectMapper().createObjectNode();

	protected MessageHeadBaseEntity(JsonNode base) {
		this.base = base;
	}

	@Override
	public JsonNode getJsonObject() {
		return this.base;
	}

	@Override
	public String getJsonString() {
		return getJsonObject().toString();
	}

	@Override
	public String getId() {
		return this.base.path(MessageConstant.MESSAGE_HEAD_BASE_ID).textValue();
	}

	@Override
	public long getTimeStamp() {
		return this.base.path(MessageConstant.MESSAGE_HEAD_BASE_TIMESTAMP).longValue();
	}

	@Override
	public String getSequence() {
		return this.base.path(MessageConstant.MESSAGE_HEAD_BASE_SEQUENCE).textValue();
	}

	@Override
	public String getCorrelationId() {
		return this.base.path(MessageConstant.MESSAGE_HEAD_BASE_CORRELATION_ID).textValue();
	}

	@Override
	public boolean isExpire() {
		if (this.base.path(MessageConstant.MESSAGE_HEAD_BASE_EXPIRE_TIME).longValue() == 0) {
			return false;
		}
		return System.currentTimeMillis()
				- getTimeStamp() > this.base.path(MessageConstant.MESSAGE_HEAD_BASE_EXPIRE_TIME).longValue() * 1000;
	}

	@Override
	public int getType() {
		return this.base.path(MessageConstant.MESSAGE_HEAD_BASE_TYPE).intValue();
	}

}
