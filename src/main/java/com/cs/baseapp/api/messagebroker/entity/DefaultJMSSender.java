/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class DefaultJMSSender extends MessageSender {

	private Connection connection = null;

	private Queue queue = null;

	public DefaultJMSSender(String id, Properties prop) {
		super(id, prop);
	}

	@Override
	public void initialize() throws BaseAppException {
		try {
			Context context = new InitialContext();
			ConnectionFactory connFactory = (ConnectionFactory) context
					.lookup(super.getProperty(ConfigConstant.JMS_CONNECTION_FACTORY_JNDI.getValue()));
			this.queue = (Queue) context.lookup(super.getProperty(ConfigConstant.JMS_QUEUE_JNDI.getValue()));
			this.connection = connFactory.createConnection();
			this.connection.start();
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo(""));
		}

	}

	@Override
	public void sendAsyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		Session session = null;
		try {
			session = this.connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(this.queue);
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			TextMessage textMsg = session.createTextMessage(requestMsg.getOutboundString());
			if (!StringUtils.isEmpty(requestMsg.getRequestMsg().getBase().getCorrelationId())) {
				textMsg.setJMSCorrelationID(requestMsg.getRequestMsg().getBase().getCorrelationId());
			}
			messageProducer.send(textMsg);
			session.commit();
		} catch (JMSException e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo(""));
		} finally {
			try {
				if (session != null) {
					session.close();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		MessageResponse resp = null;
		this.sendAsyncMessage(requestMsg);
		try {
			resp = MSBaseApplication.getMessageBroker()
					.getService(requestMsg.getRequestMsg().getServices().get(0).getId()).getReceiver().recv(requestMsg);
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo(""));
		}
		return resp;
	}

	@Override
	public void close() throws BaseAppException {
		try {
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (JMSException e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo(""));
		}
	}

}
