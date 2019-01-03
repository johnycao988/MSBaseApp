/**
 * 
 */
package com.cs.cloud.message.api;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Donald.Wang
 *
 */
public interface MessageResponseBodyService extends JsonElement {

	public String getId();

	public String getSecret();

	public String getToken();

	public String getUserName();

	public String getAppliactionId();

	public MessageResponseBodyServiceStatus getStatus();

	public JsonNode getResponseData();

}
