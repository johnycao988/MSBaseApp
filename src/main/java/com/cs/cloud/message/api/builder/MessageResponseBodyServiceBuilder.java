/**
 * 
 */
package com.cs.cloud.message.api.builder;

import com.cs.cloud.message.api.MessageResponseBodyService;
import com.cs.cloud.message.api.MessageResponseBodyServiceStatus;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 * 
 * @Description This interface is used to build the
 *              <code>MessageResponseBodyService<code>.
 *
 */
public interface MessageResponseBodyServiceBuilder extends Builder<MessageResponseBodyService> {

	public MessageResponseBodyServiceBuilder setApplicationId(String applicationId);

	public MessageResponseBodyServiceBuilder setId(String id);

	public MessageResponseBodyServiceBuilder setSecret(String secret);

	public MessageResponseBodyServiceBuilder setToken(String token);

	public MessageResponseBodyServiceBuilder setUserName(String userName);

	public MessageResponseBodyServiceBuilder setServiceStatus(MessageResponseBodyServiceStatus status);

	public MessageResponseBodyServiceBuilder setResponseData(Object resData) throws MessageException;

}
