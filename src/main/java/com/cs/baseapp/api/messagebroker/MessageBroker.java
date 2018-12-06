/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.List;

import com.cs.baseapp.api.manager.ListenerManager;
import com.cs.baseapp.api.messagebroker.event.EventManager;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.repository.BaseMessageRepository;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public interface MessageBroker {

	public Sender getSender(String id) throws BaseAppException;

	public Receiver getReceiver(String id) throws BaseAppException;

	public BaseMessageListener getListener(String id);

	public List<MBService> getServices();

	public MBService getService(String id);

	public MessageResponse invokeService(MessageRequest req) throws BaseAppException;

	public BaseMessageRepository getMessageRepository();

	public EventManager getEventManager();
	
	public ListenerManager getListenerManager();

	public void shutdown() throws BaseAppException;

}
