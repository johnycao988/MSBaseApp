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
public interface Sender {

	public void initialize() throws BaseAppException;

	public String getId();

	public void sendAsyncMessage(TranslationMessage requestMsg) throws BaseAppException;

	public void close() throws BaseAppException;

	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException;

}
