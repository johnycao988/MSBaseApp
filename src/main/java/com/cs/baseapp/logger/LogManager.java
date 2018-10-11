/**
 * 
 */
package com.cs.baseapp.logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.slf4j.LoggerFactory;

import com.cs.cloud.message.api.MessageRequest;
import com.cs.log.logs.bean.Log;
import com.cs.log.logs.bean.Logger;
import com.cs.log.logs.bean.ServiceLogKey;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * @author Donald.Wang
 *
 */
public class LogManager {

	private static String serviceName = "";

	private LogManager() {

	}

	public static ServiceLogKey getServiceLogKey(String referenceNo, String relationNo) {
		ServiceLogKey serviceLogKey = new ServiceLogKey();
		serviceLogKey.setServiceName(serviceName);
		serviceLogKey.setMsgDateTime(new Date());
		serviceLogKey.setReferenceNo(referenceNo);
		serviceLogKey.setRelationNo(relationNo);
		return serviceLogKey;
	}

	public static void init(String serviceName) {
		LogManager.serviceName = serviceName;
	}

	public static ServiceLogKey getServiceLogKey() {
		return getServiceLogKey("", "");
	}

	public static ServiceLogKey getServiceLogKey(MessageRequest req) {
		return getServiceLogKey(req.getTransaction().getReferenceNo(), req.getTransaction().getTransactionNo());
	}

	public static Logger getSystemLog() {
		return Logger.getLogger("SYSTEM");
	}

	public static Logger getSQLLogger() {
		return Logger.getLogger("SQL");
	}

	public static Log getSQLLog(MessageRequest req) {
		return new Log(getSQLLogger(), "EN", getServiceLogKey(req));
	}

	public static Log getSQLLog() {
		return new Log(getSQLLogger(), "EN", getServiceLogKey());
	}

	public static void initLogback(String externalConfigFileLocation) throws IOException, JoranException {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		File externalConfigFile = new File(externalConfigFileLocation);
		if (!externalConfigFile.exists()) {
			throw new IOException("Logback External Config File Parameter does not reference a file that exists");
		} else {
			if (!externalConfigFile.isFile()) {
				throw new IOException("Logback External Config File Parameter exists, but does not reference a file");
			} else {
				if (!externalConfigFile.canRead()) {
					throw new IOException("Logback External Config File exists and is a file, but cannot be read.");
				} else {
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(lc);
					lc.reset();
					configurator.doConfigure(externalConfigFileLocation);
					StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
				}
			}
		}
	}

}
