/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.messagebroker.BusinessService;
import com.cs.baseapp.api.messagebroker.MBService;
import com.cs.baseapp.api.messagebroker.MessageBrokerFactory;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class ServiceEntity implements MBService {

	private int type;

	private String id;

	private Properties prop;

	private String tranformClass;

	private String senderId;

	private String receiverId;

	private String implementClass;

	private boolean storeMsg;

	private Constructor<?> businessServiceConstructor;

	public ServiceEntity(Map<String, String> serviceConfig, Properties prop, boolean storeMsg) throws BaseAppException {
		this.type = Integer.parseInt(serviceConfig.get("serviceType"));
		this.id = serviceConfig.get(ConfigConstant.ID.getValue());
		this.storeMsg = storeMsg;
		this.prop = prop;
		this.tranformClass = serviceConfig.get(ConfigConstant.TRANS_CLASS.getValue());
		this.senderId = serviceConfig.get(ConfigConstant.SENDER_ID.getValue());
		this.receiverId = serviceConfig.get(ConfigConstant.RECEIVER_ID.getValue());
		this.implementClass = serviceConfig.get(ConfigConstant.IMPL_CLASS.getValue());
		if (this.type == MessageBrokerFactory.LOCAL_SERVICE) {
			this.getLocalBusinessServiceConstructor();
		}
	}

	@Override
	public int getServiceType() {
		return this.type;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Properties getProperties() {
		return this.prop;
	}

	@Override
	public String getProperty(String key) {
		return this.prop.getProperty(key);
	}

	@Override
	public String getTranformClass() {
		return this.tranformClass;
	}

	@Override
	public TranslationMessage getTranslationMessage(MessageRequest req) throws BaseAppException {
		Object instance = null;
		try {
			instance = Class.forName(this.tranformClass).getConstructor(Properties.class, MessageRequest.class)
					.newInstance(this.prop, req);
		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0027", this.id, this.tranformClass, req.getJsonString()));
		}
		return (TranslationMessage) instance;
	}

	@Override
	public MSMessageSender getSender() throws BaseAppException {
		return MSBaseApplication.getMessageBroker().getSender(this.senderId);
	}

	@Override
	public MSMessageReceiver getReceiver() throws BaseAppException {
		return MSBaseApplication.getMessageBroker().getReceiver(this.receiverId);
	}

	@Override
	public String getImplementClass() {
		return this.implementClass;
	}

	private void getLocalBusinessServiceConstructor() throws BaseAppException {
		try {
			this.businessServiceConstructor = Class.forName(this.implementClass).getConstructor(MessageRequest.class,
					Properties.class);
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0028", this.id, this.implementClass));
		}
	}

	@Override
	public BusinessService getBusinessService(MessageRequest req) throws BaseAppException {
		BusinessService bs = null;
		try {
			bs = (BusinessService) this.businessServiceConstructor.newInstance(req, this.prop);
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0015", this.id, req.getJsonString()));
		}
		return bs;
	}

	public void setStoreMsg(boolean storeMsg) {
		this.storeMsg = storeMsg;
	}

	@Override
	public boolean isStoreMsg() {
		return this.storeMsg;
	}

}
