/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import org.apache.commons.lang3.StringUtils;

import com.cs.cloud.message.api.MessageResponseBodyServiceStatus;
import com.cs.cloud.message.api.builder.MessageResponseBodyServiceStatusBuilder;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageResponseBodyServiceStatusBuilderEntity implements MessageResponseBodyServiceStatusBuilder {

	private ObjectNode status = new ObjectMapper().createObjectNode();

	@Override
	public MessageResponseBodyServiceStatus build() {
		return new MessageResponseBodyServiceStatusEntity(this.status);
	}

	@Override
	public MessageResponseBodyServiceStatusBuilder setSuccess(boolean success) {
		this.status.put(MessageConstant.MESSAGE_BODY_SERVICE_STATUS_SUCCESS, success);
		return this;
	}

	@Override
	public MessageResponseBodyServiceStatusBuilder setErrCode(String errCode) {
		if (!StringUtils.isEmpty(errCode)) {
			this.status.put(MessageConstant.MESSAGE_BODY_SERVICE_ERROR_CODE, errCode);
		}
		return this;
	}

	@Override
	public MessageResponseBodyServiceStatusBuilder setErrMsg(String errMsg) {
		if (!StringUtils.isEmpty(errMsg)) {
			this.status.put(MessageConstant.MESSAGE_BODY_SERVICE_ERROR_MESSAGE, errMsg);
		}
		return this;
	}

}
