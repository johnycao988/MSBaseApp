/**
 * 
 */
package com.cs.baseapp.api.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cs.baseapp.api.messagebroker.MBService;
import com.cs.baseapp.api.messagebroker.MessageBrokerFactory;
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
public class ServiceManager {
	private Map<String, MBService> services = new HashMap<>();

	private Logger logger = Logger.getLogger("SYSTEM");

	public ServiceManager(Map<String, MBService> services) {
		this.services = services;
	}

	public MBService getById(String id) {
		return this.services.get(id);
	}

	public List<MBService> getAll() {
		List<MBService> list = new ArrayList<>();
		Iterator<Entry<String, MBService>> it = this.services.entrySet().iterator();
		while (it.hasNext()) {
			list.add(it.next().getValue());
		}
		return list;
	}

	public MessageResponse invokeService(MessageRequest req) throws BaseAppException, MessageException {
		MessageResponse resp = null;
		MBService service = this.services.get(req.getServices().get(0).getId());
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

}
