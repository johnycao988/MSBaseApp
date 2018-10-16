/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.List;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.api.messagebroker.BaseMessageListener;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.factory.MessageFactory;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class DefaultJMSListener extends BaseMessageListener {

	private Connection connection = null;

	private Session session = null;

	private Queue queue = null;

	private MessageConsumer messageConsumer = null;

	private static Logger logger = LogManager.getSystemLog();

	public DefaultJMSListener(String id, int maxProcessThreads, Properties prop, List<MessageFilter> filters,
			int connections, String tranformClass) {
		super(id, maxProcessThreads, prop, filters, connections, tranformClass);
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
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0043", super.getId()));
		}

	}

	@Override
	public void start() throws BaseAppException {

		try {
			session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			messageConsumer = session.createConsumer(this.queue);
			messageConsumer.setMessageListener(this);
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0044", super.getId()));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}

	@Override
	public void stop() throws BaseAppException {
		try {
			if (this.messageConsumer != null) {
				this.messageConsumer.close();
			}
			if (this.session != null) {
				this.session.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (JMSException e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0042", super.getId()));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}

	@Override
	public void onMessage(Message message) {
		try {
			MessageRequest request = MessageFactory.getRequestMessage(((TextMessage) message).getText());
			super.getTranslationMessage(request);
			List<MessageFilter> filters = super.getMessageFilters();
			for (MessageFilter filter : filters) {
				filter.doListenerFilter(request);
			}
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0045", message.toString(), super.getId()));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}
}
