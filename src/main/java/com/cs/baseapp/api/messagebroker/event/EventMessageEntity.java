/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class EventMessageEntity implements EventMessage {

	private String eventId;

	private String eventName;

	private String trxId;

	private String trxBusinessDesc;

	private String creator;

	private Object evnetData;

	public EventMessageEntity(Map<String, String> messageInfo, Object eventData) {
		this.eventId = messageInfo.get(EventMessageBuilder.EVENT_ID);
		this.eventName = messageInfo.get(EventMessageBuilder.EVENT_NAME);
		this.trxId = messageInfo.get(EventMessageBuilder.TRX_ID);
		this.trxBusinessDesc = messageInfo.get(EventMessageBuilder.TRX_BUSSINESS_DESC);
		this.creator = messageInfo.get(EventMessageBuilder.CREATOR);
		this.evnetData = eventData;

	}

	@Override
	public String getEventId() {
		return this.eventId;
	}

	@Override
	public String getEventName() {
		return this.eventName;
	}

	@Override
	public String getTrxId() {
		return this.trxId;
	}

	@Override
	public String getTrxBusinessDesc() {
		return this.trxBusinessDesc;
	}

	@Override
	public String getCreator() {
		return this.creator;
	}

	@Override
	public Object getEventData() {
		return this.evnetData;
	}

	@Override
	public String getJsonString() {
		ObjectNode jsonNode = new ObjectMapper().createObjectNode();
		try {
			jsonNode.put(EventMessageBuilder.EVENT_ID, this.eventId);
			jsonNode.put(EventMessageBuilder.EVENT_NAME, this.eventName);
			jsonNode.put(EventMessageBuilder.TRX_ID, this.trxId);
			jsonNode.put(EventMessageBuilder.TRX_BUSSINESS_DESC, this.trxBusinessDesc);
			jsonNode.set(EventMessageBuilder.EVENT_DATA, new ObjectMapper().readTree((String) this.evnetData));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonNode.toString();
	}

}
