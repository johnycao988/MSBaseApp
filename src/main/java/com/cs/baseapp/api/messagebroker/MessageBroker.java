/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.List;

import com.cs.baseapp.api.messagebroker.entity.MSMessageReceiver;
import com.cs.baseapp.api.messagebroker.entity.MSMessageSender;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.repository.BaseMessageRepository;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public interface MessageBroker {

	public MSMessageSender getSender(String id) throws BaseAppException;

	public void releaseSender(MSMessageSender sender);

	public MSMessageReceiver getReceiver(String id) throws BaseAppException;

	public void releaseReceiver(MSMessageReceiver receiver);

	public List<MessageListener> getListeners();

	public MessageListener getListener(String id);

	public List<MBService> getServices();

	public MBService getService(String id);

	public MessageResponse invokeService(MessageRequest req) throws BaseAppException;

	public BaseMessageRepository getMessageRepository();

	public void shutdown();

}
