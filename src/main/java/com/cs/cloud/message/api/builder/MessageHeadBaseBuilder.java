/**
 * 
 */
package com.cs.cloud.message.api.builder;

import com.cs.cloud.message.api.MessageHeadBase;

/**
 * @author Donald.Wang
 * 
 * @Description This builder is used to build <code>MessageHeadBase<code>.
 *
 */
public interface MessageHeadBaseBuilder extends Builder<MessageHeadBase> {

	public MessageHeadBaseBuilder setId(String id);

	public MessageHeadBaseBuilder setSequence(int index, int total);

	public MessageHeadBaseBuilder setCorrelationId(String correlationId);

	public MessageHeadBaseBuilder setExpireTime(int expireTime);

	public MessageHeadBaseBuilder setType(int type);
	
}
