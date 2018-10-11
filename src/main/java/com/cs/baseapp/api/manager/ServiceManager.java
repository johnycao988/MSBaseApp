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

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.messagebroker.MBService;
import com.cs.baseapp.api.messagebroker.MessageBrokerFactory;
import com.cs.baseapp.api.messagebroker.entity.MSMessageReceiver;
import com.cs.baseapp.api.messagebroker.entity.MSMessageSender;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class ServiceManager {
	private Map<String, MBService> services = new HashMap<>();

	private Logger logger = LogManager.getSystemLog();

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

	public MessageResponse invokeService(MessageRequest req) throws BaseAppException {
		MessageResponse resp = null;
		MBService service = this.services.get(req.getServices().get(0).getId());
		logger.debug(LogManager.getServiceLogKey(req),
				"Start to process invoke Service. RequestMsg: " + req.getJsonString());
		if (service == null) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0014", req.getJsonString()));
		}
		MSMessageSender sender = null;
		MSMessageReceiver receiver = null;
		try {
			if (service.isAudit()) {
				MSBaseApplication.getMsgRepository().storeMessage(req);
			}
			if (service.getServiceType() == MessageBrokerFactory.LOCAL_SERVICE) {
				resp = service.getBusinessService(req).process();
			} else {
				if (req.getServices().get(0).isSycn()) {
					sender = service.getSender();
					resp = sender.sendSyncMessage(service.getTranslationMessage(req));
					if (resp == null) {
						receiver = service.getReceiver();
						resp = receiver.recv(service.getTranslationMessage(req));

					}
				} else {
					sender = service.getSender();
					sender.sendAsyncMessage(service.getTranslationMessage(req));
				}
			}
			if (service.isAudit()) {
				MSBaseApplication.getMsgRepository().storeMessage(resp);
			}
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0016", req.getJsonString()));
			logger.write(LogManager.getServiceLogKey(req), ex);
		} finally {
			if (sender != null) {
				sender.close();
			}
			if (receiver != null) {
				receiver.close();
			}
		}
		logger.debug(LogManager.getServiceLogKey(req),
				"Invoke Service success. RespMsg:" + (resp == null ? "" : resp.getJsonString()));
		return resp;
	}

}
