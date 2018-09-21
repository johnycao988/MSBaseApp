/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.Properties;

import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.httpclient.BaseHttpClient;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;
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
		throw new BaseAppException(new UnsupportedOperationException(),
				LogInfoMgr.getErrorInfo("ERR_0011", requestMsg.getOutboundString()));
	}

	@Override
	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException, MessageException {
		String url = requestMsg.getProperty("URI");
		BaseHttpClient http = new BaseHttpClient();
		return MessageFactory.getResopnseMessage(http.post(url, null, requestMsg.getOutboundString()));
	}

	@Override
	public void close() {
		// Do nothing
	}

}
