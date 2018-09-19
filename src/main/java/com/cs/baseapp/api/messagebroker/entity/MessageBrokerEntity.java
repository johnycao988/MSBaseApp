/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.List;
import java.util.Map;

import com.cs.baseapp.api.manager.ListenerManager;
import com.cs.baseapp.api.manager.ReceiverManager;
import com.cs.baseapp.api.manager.SenderManager;
import com.cs.baseapp.api.manager.ServiceManager;
import com.cs.baseapp.api.messagebroker.MBService;
import com.cs.baseapp.api.messagebroker.MessageBroker;
import com.cs.baseapp.api.messagebroker.MessageListener;
import com.cs.baseapp.api.messagebroker.MessageReceiver;
import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public class MessageBrokerEntity implements MessageBroker {

	private SenderManager senderManager;

	private ReceiverManager receiverManager;

	private ListenerManager listenerManager;

	private ServiceManager serviceManager;

	public MessageBrokerEntity(Map<String, MessageSender> senders, Map<String, MessageReceiver> receivers,
			Map<String, MessageListener> listeners, Map<String, MBService> services) {
		this.senderManager = new SenderManager(senders);
		this.receiverManager = new ReceiverManager(receivers);
		this.listenerManager = new ListenerManager(listeners);
		this.serviceManager = new ServiceManager(services);
	}

	@Override
	public List<MessageSender> getSenders() {
		return this.senderManager.getAll();
	}

	@Override
	public MessageSender getSender(String id) {
		return this.senderManager.getById(id);
	}

	@Override
	public List<MessageReceiver> getReceivers() {
		return this.receiverManager.getAll();
	}

	@Override
	public MessageReceiver getReceiver(String id) {
		return this.receiverManager.getById(id);
	}

	@Override
	public List<MessageListener> getListeners() {
		return this.listenerManager.getAll();
	}

	@Override
	public MessageListener getListener(String id) {
		return this.listenerManager.getById(id);
	}

	@Override
	public List<MBService> getServices() {
		return this.serviceManager.getAll();
	}

	@Override
	public MBService getService(String id) {
		return this.serviceManager.getById(id);
	}

	@Override
	public MessageResponse invokeService(MessageRequest req) throws BaseAppException, MessageException {
		return this.serviceManager.invokeService(req);
	}

	@Override
	public void shutdown() {

	}

}
