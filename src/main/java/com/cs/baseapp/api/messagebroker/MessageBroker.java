/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.List;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public interface MessageBroker {

	public List<MessageSender> getSenders();

	public MessageSender getSender(String id);

	public List<MessageReceiver> getReceivers();

	public MessageReceiver getReceiver(String id);

	public List<MessageListener> getListeners();

	public MessageListener getListener(String id);

	public List<MBService> getServices();

	public MBService getService(String id);

	public MessageResponse invokeService(MessageRequest req) throws BaseAppException, MessageException;

	public void shutdown();

}
