/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.Properties;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;

/**
 * @author Donald.Wang
 *
 */
public interface MBService {

	public int getServiceType();

	public String getId();

	public Properties getProperties();

	public String getProperty(String key);

	public String getTranformClass();

	public TranslationMessage getTranslationMessage(MessageRequest req) throws BaseAppException;

	public MessageSender getSender() throws BaseAppException;

	public MessageReceiver getReceiver() throws BaseAppException;

	public String getImplementClass();

	public BusinessService getBusinessService(MessageRequest req) throws BaseAppException;

}
