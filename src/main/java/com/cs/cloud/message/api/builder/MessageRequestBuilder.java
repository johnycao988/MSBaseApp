/**
 * 
 */
package com.cs.cloud.message.api.builder;

import java.util.List;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.MessageHeadConsumer;
import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.api.MessageHeadTransaction;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageRequestBodyService;

/**
 * @author Donald.Wang
 * 
 * @Description This builder is used to build <code>MessageRequest<code>.
 *
 */
public interface MessageRequestBuilder extends Builder<MessageRequest> {

	/**
	 * @param base
	 * @return MessageRequestBuilder
	 * @see MessageHeadBase
	 */
	public MessageRequestBuilder setBase(MessageHeadBase base);

	/**
	 * @param consumer
	 * @return MessageRequestBuilder
	 * @see MessageHeadConsumer
	 */
	public MessageRequestBuilder setConsumer(MessageHeadConsumer consumer);

	public MessageRequestBuilder addService(MessageHeadService service);

	public MessageRequestBuilder setServices(List<MessageHeadService> services);

	public MessageRequestBuilder setTransaction(MessageHeadTransaction trx);

	public MessageRequestBuilder addBodyService(MessageRequestBodyService bodyService);

	public MessageRequestBuilder setBodyServices(List<MessageRequestBodyService> bodyServices);

	public MessageHeadBaseBuilder getBaseBuilder();

	public MessageHeadConsumerBuilder getConsumerBuilder();

	public MessageHeadServiceBuilder getServiceBuilder();

	public MessageHeadTransactionBuilder getTransactionBuilder();

	public MessageRequestBodyServiceBuilder getBodyServiceBuilder();

}
