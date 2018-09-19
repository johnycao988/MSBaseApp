/**
 * 
 */
package com.cs.baseapp.logger;

import java.util.Date;

import com.cs.cloud.message.api.MessageRequest;
import com.cs.log.logs.bean.ServiceLogKey;

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

}
