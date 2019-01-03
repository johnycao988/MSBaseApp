/**
 * 
 */
package com.cs.cloud.message.api;

/**
 * @author Donald.Wang
 * 
 * @Description The <code>MessageHeadConsumer<code> is a part of message header.
 *              It stores the information of message consumer.
 *
 */
public interface MessageHeadConsumer extends JsonElement {
	
	public String getClientId();

	public String getId();

	public String getSecret();

	public String getToken();

	public String getUserName();
}
