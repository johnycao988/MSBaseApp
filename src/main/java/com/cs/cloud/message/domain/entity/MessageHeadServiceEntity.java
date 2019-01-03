/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class MessageHeadServiceEntity implements MessageHeadService {

	private JsonNode service = new ObjectMapper().createObjectNode();

	protected MessageHeadServiceEntity(JsonNode service) {
		this.service = service;
	}

	@Override
	public JsonNode getJsonObject() {
		return this.service;
	}

	@Override
	public String getJsonString() {
		return getJsonObject().toString();
	}

	@Override
	public String getId() {
		return this.service.path(MessageConstant.MESSAGE_HEAD_SERVICE_ID).textValue();
	}

	@Override
	public String getApplicationId() {
		return this.service.path(MessageConstant.MESSAGE_HEAD_SERVICE_APPLICATION_ID).textValue();
	}

	@Override
	public Map<String, String> getProperties() {
		JsonNode properties = this.service.path(MessageConstant.MESSAGE_HEAD_SERVICE_PROPERTIES);
		Map<String, String> propertiesMap = new HashMap<>();
		Iterator<String> names = properties.fieldNames();
		String name;
		while (names.hasNext()) {
			name = names.next();
			propertiesMap.put(name, properties.get(name).textValue());
		}
		return propertiesMap;
	}

	@Override
	public boolean isSycn() {
		return this.service.path(MessageConstant.MESSAGE_HEAD_SERVICE_SYNC).booleanValue();
	}

	@Override
	public String getProperty(String key) {
		return this.getProperties().get(key);
	}

}
