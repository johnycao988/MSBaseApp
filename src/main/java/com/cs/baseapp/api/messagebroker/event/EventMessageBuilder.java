/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class EventMessageBuilder {

	private Map<String, String> eventInfo = new HashMap<>();

	private Object eventData;

	public static final String EVENT_ID = "eventId";
	public static final String EVENT_NAME = "eventName";
	public static final String TRX_ID = "trxId";
	public static final String TRX_BUSSINESS_DESC = "trxBusDesc";
	public static final String CREATOR = "creator";
	public static final String EVENT_DATA = "eventData";

	public EventMessageBuilder setEventId(String eventId) {
		this.eventInfo.put(EVENT_ID, eventId);
		return this;
	}

	public EventMessageBuilder setEventName(String eventName) {
		this.eventInfo.put(EVENT_NAME, eventName);
		return this;
	}

	public EventMessageBuilder setTrxId(String trxId) {
		this.eventInfo.put(TRX_ID, trxId);
		return this;
	}

	public EventMessageBuilder setTrxBussinessDesc(String desc) {
		this.eventInfo.put(TRX_BUSSINESS_DESC, desc);
		return this;
	}

	public EventMessageBuilder setCreator(String creator) {
		this.eventInfo.put(CREATOR, creator);
		return this;
	}

	public EventMessageBuilder setEventData(Object evnetData) {
		this.eventData = evnetData;
		return this;
	}

	public EventMessage build() {
		return new EventMessageEntity(this.eventInfo, this.eventData);
	}

	public EventMessage getEventMessage(String jsonEventMessage) {
		EventMessageEntity eventMsg = null;
		try {
			JsonNode jsonMsg = new ObjectMapper().readTree(jsonEventMessage);
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put(EVENT_ID, jsonMsg.path(EVENT_ID).asText());
			paraMap.put(EVENT_NAME, jsonMsg.path(EVENT_NAME).asText());
			paraMap.put(TRX_ID, jsonMsg.path(TRX_ID).asText());
			paraMap.put(TRX_BUSSINESS_DESC, jsonMsg.path(TRX_BUSSINESS_DESC).asText());
			paraMap.put(CREATOR, jsonMsg.path(CREATOR).asText());
			eventMsg = new EventMessageEntity(paraMap, jsonMsg.path(EVENT_DATA).asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return eventMsg;
	}

}
