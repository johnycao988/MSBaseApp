/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;

/**
 * @author Donald.Wang
 *
 */
public abstract class TranslationMessage {

	private MessageRequest msgReqeust;

	private Properties prop;

	public abstract String getOutboundString() throws BaseAppException;

	public abstract byte[] getOutboundBytes() throws BaseAppException;

	public abstract Object getOutboundObject() throws BaseAppException;

	public abstract InputStream getOutboundInputStream() throws BaseAppException;

	public abstract MessageResponse getInboundMessage(Object msgResponse) throws BaseAppException, MessageException;

	public TranslationMessage(Properties prop, MessageRequest msgRequest) {
		this.prop = prop;
		this.msgReqeust = msgRequest;
	}

	public String getProperty(String key) {
		String value = null;
		if (this.msgReqeust != null) {
			value = this.msgReqeust.getServices().get(0).getProperties().get(key);
		}
		if (StringUtils.isEmpty(value)) {
			value = this.prop.getProperty(key);
		}
		return value;
	}

	public MessageRequest getRequestMsg() {
		return msgReqeust;
	}

}
