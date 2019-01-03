/**
 * 
 */
package com.cs.cloud.message.api;

import java.util.List;

/**
 * @author Donald.Wang
 *
 */
public interface MessageRequest extends JsonElement {

	public MessageHeadBase getBase();

	public MessageHeadConsumer getConsumer();

	public List<MessageHeadService> getServices();

	public MessageHeadTransaction getTransaction();

	public MessageRequestBodyService getBodyService(String serviceId);

	public List<MessageRequestBodyService> getBodyServices();

	public MessageRequestBodyService getFirstBodyService();
}
