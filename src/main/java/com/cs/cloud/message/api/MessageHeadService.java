/**
 * 
 */
package com.cs.cloud.message.api;

import java.util.Map;

/**
 * @author Donald.Wang
 *
 */
public interface MessageHeadService extends JsonElement {

	public String getId();

	public String getApplicationId();

	public Map<String, String> getProperties();

	public String getProperty(String key);

	public boolean isSycn();

}
