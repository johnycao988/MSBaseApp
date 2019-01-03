/**
 * 
 */
package com.cs.cloud.message.api.builder;

import com.cs.cloud.message.api.MessageHeadConsumer;

/**
 * @author Donald.Wang
 * 
 * @Description This builder is used to build <code>MessageHeadConsumer<code>.
 *
 */
public interface MessageHeadConsumerBuilder extends Builder<MessageHeadConsumer> {

	public MessageHeadConsumerBuilder setClientId(String clientId);

	public MessageHeadConsumerBuilder setId(String id);

	public MessageHeadConsumerBuilder setSecret(String secret);

	public MessageHeadConsumerBuilder setToken(String token);

	public MessageHeadConsumerBuilder setUserName(String userName);

}
