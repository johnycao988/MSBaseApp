/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.api.MessageResponseBodyService;
import com.cs.cloud.message.api.builder.MessageHeadBaseBuilder;
import com.cs.cloud.message.api.builder.MessageResponseBodyServiceBuilder;
import com.cs.cloud.message.api.builder.MessageResponseBodyServiceStatusBuilder;
import com.cs.cloud.message.api.builder.MessageResponseBuilder;

/**
 * @author Donald.Wang
 *
 */
public class MessageResponseBuilderEntity implements MessageResponseBuilder {

	private MessageHeadBase base;
	private List<MessageResponseBodyService> bodyServices = new ArrayList<>();

	@Override
	public MessageResponse build() {
		return new MessageResponseEntity(this.base, this.bodyServices);
	}

	@Override
	public MessageResponseBuilder setBase(MessageHeadBase base) {
		this.base = base;
		return this;
	}

	@Override
	public MessageResponseBuilder addResponseBodyService(MessageResponseBodyService service) {
		this.bodyServices.add(service);
		return this;
	}

	@Override
	public MessageResponseBuilder setResponseBodyServices(List<MessageResponseBodyService> services) {
		this.bodyServices = services;
		return this;
	}

	@Override
	public MessageHeadBaseBuilder getMessageHeadBaseBuilder() {
		return new MessageHeadBaseBuilderEntity();
	}

	@Override
	public MessageResponseBodyServiceBuilder getMessageResponseBodyServiceBuilder() {
		return new MessageResponseBodyServiceBuilderEntity();
	}

	@Override
	public MessageResponseBodyServiceStatusBuilder getMessageResponseBodyServiceStatusBuilder() {
		return new MessageResponseBodyServiceStatusBuilderEntity();
	}

}
