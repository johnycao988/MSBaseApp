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
import com.cs.baseapp.errorhandling.BaseAppException;
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

	public MessageBrokerEntity(List<Map<String, Object>> sendersConfig, List<Map<String, Object>> receiverConfig,
			Map<String, MessageListener> listeners, Map<String, MBService> services) {
		this.senderManager = new SenderManager(sendersConfig);
		this.receiverManager = new ReceiverManager(receiverConfig);
		this.listenerManager = new ListenerManager(listeners);
		this.serviceManager = new ServiceManager(services);
	}

	@Override
	public MSMessageSender getSender(String id) throws BaseAppException {
		return this.senderManager.getSender(id);
	}

	@Override
	public MSMessageReceiver getReceiver(String id) throws BaseAppException {
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
	public MessageResponse invokeService(MessageRequest req) throws BaseAppException {
		return this.serviceManager.invokeService(req);
	}

	@Override
	public void shutdown() {

	}

	@Override
	public void releaseSender(MSMessageSender sender) {
		this.senderManager.releaseSender(sender);
	}

	@Override
	public void releaseReceiver(MSMessageReceiver receiver) {
		this.receiverManager.release(receiver);

	}

}
