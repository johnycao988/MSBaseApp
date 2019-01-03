/**
 * 
 */
package com.cs.cloud.message.api.builder;

import java.util.List;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.api.MessageResponseBodyService;

/**
 * @author Donald.Wang
 *
 *
 * @Description This builder is used to build <code>MessageResponse<code>.
 */
public interface MessageResponseBuilder extends Builder<MessageResponse> {

	public MessageResponseBuilder setBase(MessageHeadBase base);

	public MessageResponseBuilder addResponseBodyService(MessageResponseBodyService service);

	public MessageResponseBuilder setResponseBodyServices(List<MessageResponseBodyService> services);

	public MessageHeadBaseBuilder getMessageHeadBaseBuilder();

	public MessageResponseBodyServiceBuilder getMessageResponseBodyServiceBuilder();

	public MessageResponseBodyServiceStatusBuilder getMessageResponseBodyServiceStatusBuilder();

}
