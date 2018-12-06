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
import com.cs.baseapp.api.messagebroker.BaseMessageListener;
import com.cs.baseapp.api.messagebroker.MBService;
import com.cs.baseapp.api.messagebroker.MessageBroker;
import com.cs.baseapp.api.messagebroker.Receiver;
import com.cs.baseapp.api.messagebroker.Sender;
import com.cs.baseapp.api.messagebroker.event.EventManager;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.repository.BaseMessageRepository;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;

/**
 * @author Donald.Wang
 *
 */
public class MessageBrokerEntity implements MessageBroker {

	private SenderManager senderManager;

	private ReceiverManager receiverManager;

	private ListenerManager listenerManager;

	private ServiceManager serviceManager;

	private EventManager eventManager;

	private BaseMessageRepository baseMessageRepository;

	public MessageBrokerEntity(List<Map<String, Object>> sendersConfig, List<Map<String, Object>> receiverConfig,
			List<Map<String, Object>> listenerConfig, List<Map<String, Object>> eventManagersConfig,
			Map<String, MBService> services, BaseMessageRepository repository) {
		this.senderManager = new SenderManager(sendersConfig);
		this.receiverManager = new ReceiverManager(receiverConfig);
		this.listenerManager = new ListenerManager(listenerConfig);
		this.serviceManager = new ServiceManager(services);
		this.eventManager = new EventManager(eventManagersConfig);
		this.baseMessageRepository = repository;
	}

	@Override
	public Sender getSender(String id) throws BaseAppException {
		return this.senderManager.getSender(id);
	}

	@Override
	public Receiver getReceiver(String id) throws BaseAppException {
		return this.receiverManager.getById(id);
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
	public MessageResponse invokeService(MessageRequest req) throws BaseAppException {
		return this.serviceManager.invokeService(req);
	}

	@Override
	public void shutdown() throws BaseAppException {
		this.serviceManager.stop();
		this.senderManager.stop();
		this.receiverManager.stop();
		this.listenerManager.stopAll();
	}

	@Override
	public BaseMessageRepository getMessageRepository() {
		return this.baseMessageRepository;
	}

	@Override
	public EventManager getEventManager() {
		return this.eventManager;
	}

	@Override
	public BaseMessageListener getListener(String id) {
		return this.listenerManager.getById(id);
	}

	@Override
	public ListenerManager getListenerManager() {
		return this.listenerManager;
	}

}
