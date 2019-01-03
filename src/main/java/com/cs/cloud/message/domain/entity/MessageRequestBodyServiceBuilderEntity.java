/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import org.apache.commons.lang3.StringUtils;

import com.cs.cloud.message.api.MessageRequestBodyService;
import com.cs.cloud.message.api.builder.MessageRequestBodyServiceBuilder;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageRequestBodyServiceBuilderEntity implements MessageRequestBodyServiceBuilder {

	ObjectNode bodyService = new ObjectMapper().createObjectNode();

	@Override
	public MessageRequestBodyService build() {
		return new MessageRequestBodyServiceEntity(this.bodyService);
	}

	@Override
	public MessageRequestBodyServiceBuilder setServiceId(String serviceId) {
		if (!StringUtils.isEmpty(serviceId)) {
			this.bodyService.put(MessageConstant.MESSAGE_BODY_SERVICE_ID, serviceId);
		}
		return this;
	}

	@Override
	public MessageRequestBodyServiceBuilder setRequestData(Object reqData) throws MessageException {
		if (reqData == null) {
			return this;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(reqData);
			bodyService.set(MessageConstant.MESSAGE_BODY_SERVICE_REQUEST_DATA, mapper.readTree(json));
		} catch (Exception e) {
			throw new MessageException("Invalid Request Data!", e);
		}
		return this;
	}

}
