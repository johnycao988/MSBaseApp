/**
 * 
 */
package com.cs.cloud.message.api.builder;

import com.cs.cloud.message.api.MessageRequestBodyService;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 * 
 * @Description This builder is used to build
 *              <code>MessageRequestBodyService<code>.
 *
 */
public interface MessageRequestBodyServiceBuilder extends Builder<MessageRequestBodyService> {

	public MessageRequestBodyServiceBuilder setServiceId(String serviceId);

	public MessageRequestBodyServiceBuilder setRequestData(Object reqData) throws MessageException;

}
