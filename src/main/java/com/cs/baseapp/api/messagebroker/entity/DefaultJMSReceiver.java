/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.api.messagebroker.MessageReceiver;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class DefaultJMSReceiver extends MessageReceiver {

	private Connection connection = null;

	private Queue queue = null;

	private Logger logger = LogManager.getSystemLog();

	public DefaultJMSReceiver(String id, Properties prop) {
		super(id, prop);
	}

	@Override
	public void initialize() throws BaseAppException {
		try {
			Context context = new InitialContext();
			ConnectionFactory connFactory = (ConnectionFactory) context.lookup(
					super.getProperty(super.getProperty(ConfigConstant.JMS_CONNECTION_FACTORY_JNDI.getValue())));
			this.queue = (Queue) context
					.lookup(super.getProperty(ConfigConstant.JMS_CONNECTION_FACTORY_JNDI.getValue()));
			this.connection = connFactory.createConnection();
			this.connection.start();
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0018", super.getId()));
		}
	}

	@Override
	public MessageResponse recv(TranslationMessage msgRequest) throws BaseAppException {
		Session session = null;
		try {
			session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer messageConsumer = session.createConsumer(this.queue,
					"correlationID='" + msgRequest.getRequestMsg().getBase().getId() + "'");
			String strTimeout = super.prop.getProperty(ConfigConstant.TIMEOUT.getValue());
			long timeout = Long.parseLong(!StringUtils.isEmpty(strTimeout) ? strTimeout : "6000");
			messageConsumer.receive(timeout);
		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0019", super.getId(), msgRequest.getRequestMsg().getJsonString()));
		} finally {
			try {
				if (session != null) {
					session.close();
				}
			} catch (JMSException e) {
				BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0021", super.getId()));
				logger.write(LogManager.getServiceLogKey(msgRequest.getRequestMsg()), ex);
			}
		}
		return null;
	}

	@Override
	public void close() throws BaseAppException {
		try {
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (JMSException e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0020", super.getId()));
		}
	}

}
