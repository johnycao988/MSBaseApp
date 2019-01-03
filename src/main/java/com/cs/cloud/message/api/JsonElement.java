/**
 * 
 */
package com.cs.cloud.message.api;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Donald.Wang
 *
 */
public interface JsonElement {

	public JsonNode getJsonObject();

	public String getJsonString();

}
