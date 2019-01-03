/**
 * 
 */
package com.cs.cloud.message.api.builder;

import com.cs.cloud.message.api.MessageResponseBodyServiceStatus;

/**
 * @author Donald.Wang
 * 
 * @Description This builder is used to build <code>MessageResponseBodyServiceStatus<code>.
 *
 */
public interface MessageResponseBodyServiceStatusBuilder extends Builder<MessageResponseBodyServiceStatus> {

	public MessageResponseBodyServiceStatusBuilder setSuccess(boolean success);

	public MessageResponseBodyServiceStatusBuilder setErrCode(String errCode);

	public MessageResponseBodyServiceStatusBuilder setErrMsg(String errMsg);

}
