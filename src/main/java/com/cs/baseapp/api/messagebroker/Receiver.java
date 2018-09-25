/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public interface Receiver {

	public void initialize() throws BaseAppException;

	public MessageResponse recv(TranslationMessage msgRequest) throws BaseAppException;

	public void close() throws BaseAppException;

}
