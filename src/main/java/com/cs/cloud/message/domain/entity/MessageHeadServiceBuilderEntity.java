/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.api.builder.MessageHeadServiceBuilder;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageHeadServiceBuilderEntity implements MessageHeadServiceBuilder {

	private ObjectNode service = new ObjectMapper().createObjectNode();

	@Override
	public MessageHeadService build() {
		return new MessageHeadServiceEntity(this.service);
	}

	@Override
	public MessageHeadServiceBuilder setId(String id) {
		if (!StringUtils.isEmpty(id)) {
			this.service.put(MessageConstant.MESSAGE_HEAD_SERVICE_ID, id);
		}
		return this;
	}

	@Override
	public MessageHeadServiceBuilder setApplicationId(String applicationId) {
		if (!StringUtils.isEmpty(applicationId)) {
			this.service.put(MessageConstant.MESSAGE_HEAD_SERVICE_APPLICATION_ID, applicationId);
		}
		return this;
	}

	@Override
	public MessageHeadServiceBuilder setProperties(Map<String, String> properties) {
		if (properties == null || properties.isEmpty()) {
			return this;
		}
		ObjectNode jsonProperties = new ObjectMapper().createObjectNode();
		for (Entry<String, String> entry : properties.entrySet()) {
			jsonProperties.put(entry.getKey(), entry.getValue());
		}
		this.service.set(MessageConstant.MESSAGE_HEAD_SERVICE_PROPERTIES, jsonProperties);
		return this;
	}

	@Override
	public MessageHeadServiceBuilder setProperty(String name, String value) {
		ObjectNode newProperties = new ObjectMapper().createObjectNode();
		JsonNode oldProperties = this.service.path(MessageConstant.MESSAGE_HEAD_SERVICE_PROPERTIES);
		Iterator<String> iteratorNames = oldProperties.fieldNames();
		while (iteratorNames.hasNext()) {
			String key = iteratorNames.next();
			newProperties.put(key, oldProperties.get(key).textValue());
		}
		newProperties.put(name, value);
		this.service.set(MessageConstant.MESSAGE_HEAD_SERVICE_PROPERTIES, newProperties);
		return this;
	}

	@Override
	public MessageHeadServiceBuilder setSync(boolean isSync) {
		this.service.put(MessageConstant.MESSAGE_HEAD_SERVICE_SYNC, isSync);
		return this;
	}

}
