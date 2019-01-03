/**
 * 
 */
package com.cs.cloud.message.api.builder;

import java.util.Map;

import com.cs.cloud.message.api.MessageHeadService;

/**
 * @author Donald.Wang
 * 
 * @Description This builder is used to build <code>MessageHeadService<code>.
 *
 */
public interface MessageHeadServiceBuilder extends Builder<MessageHeadService> {

	public MessageHeadServiceBuilder setId(String id);

	public MessageHeadServiceBuilder setApplicationId(String applicationId);

	public MessageHeadServiceBuilder setProperties(Map<String, String> properties);

	public MessageHeadServiceBuilder setProperty(String name, String value);

	public MessageHeadServiceBuilder setSync(boolean isSync);

}
