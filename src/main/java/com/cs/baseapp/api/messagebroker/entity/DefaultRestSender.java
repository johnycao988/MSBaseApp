/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.httpclient.BaseHttpClient;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.factory.MessageFactory;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class DefaultRestSender extends MessageSender {

	public DefaultRestSender(String id, Properties prop) {
		super(id, prop);
	}

	@Override
	public void initialize() throws BaseAppException {
		// Do nothing because of the rest sender do not need initialize;
	}

	@Override
	public void sendAsyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		// do nothing
	}

	@Override
	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		String uri = requestMsg.getProperty(ConfigConstant.URI.getValue());
		BaseHttpClient http = new BaseHttpClient();
		MessageResponse resp = null;
		if (StringUtils.isEmpty(uri)) {
			uri = super.getProperty(ConfigConstant.URI.getValue());
		}
		try {
			if ("GET".equalsIgnoreCase(super.getProperty(ConfigConstant.REST_METHOD.getValue()))) {
				resp = MessageFactory.getResopnseMessage(http.get(uri));
			} else {
				resp = MessageFactory.getResopnseMessage(http.post(uri, null, requestMsg.getOutboundString()));
			}
		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0026", super.getId(), requestMsg.getRequestMsg().getJsonString()));
		}
		return resp;
	}

	@Override
	public void close() {
		// Do nothing
	}

}
