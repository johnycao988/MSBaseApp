/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.cloud.message.domain.factory.MessageFactory;

/**
 * @author Donald.Wang
 *
 */
public class DefaultTranslationMessage extends TranslationMessage {

	public DefaultTranslationMessage(Properties prop, MessageRequest msgRequest) {
		super(prop, msgRequest);
	}

	@Override
	public String getOutboundString() throws BaseAppException {
		return super.getRequestMsg().getJsonString();
	}

	@Override
	public byte[] getOutboundBytes() throws BaseAppException {
		return this.getOutboundString().getBytes();
	}

	@Override
	public Object getOutboundObject() throws BaseAppException {
		return super.getRequestMsg();
	}

	@Override
	public InputStream getOutboundInputStream() throws BaseAppException {
		return new ByteArrayInputStream(this.getOutboundString().getBytes());
	}

	@Override
	public MessageResponse getInboundMessage(Object msgResponse) throws BaseAppException, MessageException {
		return MessageFactory.getResopnseMessage((String) msgResponse);
	}

}
