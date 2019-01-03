/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.MessageHeadConsumer;
import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.api.MessageHeadTransaction;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageRequestBodyService;
import com.cs.cloud.message.api.builder.MessageHeadBaseBuilder;
import com.cs.cloud.message.api.builder.MessageHeadConsumerBuilder;
import com.cs.cloud.message.api.builder.MessageHeadServiceBuilder;
import com.cs.cloud.message.api.builder.MessageHeadTransactionBuilder;
import com.cs.cloud.message.api.builder.MessageRequestBodyServiceBuilder;
import com.cs.cloud.message.api.builder.MessageRequestBuilder;

/**
 * @author Donald.Wang
 *
 */
public class MessageRequestBuilderEntity implements MessageRequestBuilder {

	private MessageHeadBase base;

	private MessageHeadConsumer consumer;

	private List<MessageHeadService> headServices = new ArrayList<>();

	private MessageHeadTransaction trx;

	private List<MessageRequestBodyService> bodyServices = new ArrayList<>();

	@Override
	public MessageRequest build() {
		return new MessageRequestEntity(this.base, this.consumer, this.headServices, this.trx, this.bodyServices);
	}

	@Override
	public MessageRequestBuilder setBase(MessageHeadBase base) {
		this.base = base;
		return this;
	}

	@Override
	public MessageRequestBuilder setConsumer(MessageHeadConsumer consumer) {
		this.consumer = consumer;
		return this;
	}

	@Override
	public MessageRequestBuilder addService(MessageHeadService service) {
		this.headServices.add(service);
		return this;
	}

	@Override
	public MessageRequestBuilder setServices(List<MessageHeadService> services) {
		this.headServices = services;
		return this;
	}

	@Override
	public MessageRequestBuilder setTransaction(MessageHeadTransaction trx) {
		this.trx = trx;
		return this;
	}

	@Override
	public MessageRequestBuilder addBodyService(MessageRequestBodyService bodyService) {
		this.bodyServices.add(bodyService);
		return this;
	}

	@Override
	public MessageRequestBuilder setBodyServices(List<MessageRequestBodyService> bodyServices) {
		this.bodyServices = bodyServices;
		return this;
	}

	@Override
	public MessageHeadBaseBuilder getBaseBuilder() {
		return new MessageHeadBaseBuilderEntity();
	}

	@Override
	public MessageHeadConsumerBuilder getConsumerBuilder() {
		return new MessageHeadConsumerBuilderEntity();
	}

	@Override
	public MessageHeadServiceBuilder getServiceBuilder() {
		return new MessageHeadServiceBuilderEntity();
	}

	@Override
	public MessageHeadTransactionBuilder getTransactionBuilder() {
		return new MessageHeadTransactionBuilderEntity();
	}

	@Override
	public MessageRequestBodyServiceBuilder getBodyServiceBuilder() {
		return new MessageRequestBodyServiceBuilderEntity();
	}

}
