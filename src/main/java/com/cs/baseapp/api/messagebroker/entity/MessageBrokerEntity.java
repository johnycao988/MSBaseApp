/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.List;
import java.util.Map;

import com.cs.baseapp.api.messagebroker.MBService;
import com.cs.baseapp.api.messagebroker.MessageBroker;
import com.cs.baseapp.api.messagebroker.MessageBrokerFactory;
import com.cs.baseapp.api.messagebroker.MessageListener;
import com.cs.baseapp.api.messagebroker.MessageReceiver;
import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.repository.ListenerManager;
import com.cs.baseapp.api.repository.ReceiverRepository;
import com.cs.baseapp.api.repository.SenderRepository;
import com.cs.baseapp.api.repository.ServiceRepository;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class MessageBrokerEntity implements MessageBroker {

	private SenderRepository senderRepository;

	private ReceiverRepository receiverRepository;

	private ListenerManager listenerManager;

	private ServiceRepository serviceRepository;

	private Logger logger = Logger.getLogger("SYSTEM");

	public MessageBrokerEntity(Map<String, MessageSender> senders, Map<String, MessageReceiver> receivers,
			Map<String, MessageListener> listeners, Map<String, MBService> services) {
		this.senderRepository = new SenderRepository(senders);
		this.receiverRepository = new ReceiverRepository(receivers);
		this.listenerManager = new ListenerManager(listeners);
		this.serviceRepository = new ServiceRepository(services);
	}

	@Override
	public List<MessageSender> getSenders() {
		return this.senderRepository.getAll();
	}

	@Override
	public MessageSender getSender(String id) {
		return this.senderRepository.getById(id);
	}

	@Override
	public List<MessageReceiver> getReceivers() {
		return this.receiverRepository.getAll();
	}

	@Override
	public MessageReceiver getReceiver(String id) {
		return this.receiverRepository.getById(id);
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
		return this.serviceRepository.getAll();
	}

	@Override
	public MBService getService(String id) {
		return this.serviceRepository.getById(id);
	}

	@Override
	public MessageResponse invokeService(MessageRequest req) throws BaseAppException, MessageException {
		MessageResponse resp = null;
		MBService service = this.serviceRepository.getById(req.getServices().get(0).getId());
		logger.debug(LogManager.getServiceLogKey(req),
				"Start to process invoke Service. RequestMsg: " + req.getJsonString());
		if (service == null) {
			logger.warn(LogManager.getServiceLogKey(req),
					"Can not get the information of this service.  RequestMsg:" + req.getJsonString());
			return resp;
		}
		try {
			if (service.getServiceType() == MessageBrokerFactory.LOCAL_SERVICE) {
				resp = service.getBusinessService(req).process();
			} else {
				if (req.getServices().get(0).isSycn()) {
					resp = service.getSender().sendSyncMessage(service.getTranslationMessage(req));
					if (resp == null) {
						resp = service.getReceiver().recv(service.getTranslationMessage(req));
					}
				} else {
					service.getSender().sendAsyncMessage(service.getTranslationMessage(req));
				}
			}
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0012", service.getId(), req.getJsonString()));
		}
		logger.debug(LogManager.getServiceLogKey(req),
				"Invoke Service success. RespMsg:" + (resp == null ? "" : resp.getJsonString()));
		return resp;
	}

	@Override
	public void shutdown() {

	}

}
