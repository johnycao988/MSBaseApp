/**
 * 
 */
package com.cs.cloud.message.api;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Donald.Wang
 *
 */
public interface MessageRequestBodyService extends JsonElement {

	public String getServiceId();

	public JsonNode getRequestData();

}
