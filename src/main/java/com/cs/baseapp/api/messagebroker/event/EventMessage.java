/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

/**
 * @author Donald.Wang
 *
 */
public interface EventMessage {

	public String getEventId();

	public String getEventName();

	public String getTrxId();

	public String getTrxBusinessDesc();

	public String getCreator();

	public Object getEventData();

	public String getJsonString();

}
