/**
 * 
 */
package com.cs.cloud.message.api;

import java.util.List;

/**
 * @author Donald.Wang
 *
 */
public interface MessageResponse extends JsonElement {

	public MessageHeadBase getBase();

	public List<MessageResponseBodyService> getBodyServices();

	public MessageResponseBodyService getFirstBodyService();

	public MessageResponseBodyService getBodyService(String serviceId);
}
