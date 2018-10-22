/**
 * 
 */
package com.cs.unit.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.messagebroker.MBService;
import com.cs.baseapp.api.messagebroker.Sender;
import com.cs.baseapp.api.messagebroker.entity.MSMessageSender;
import com.cs.cloud.message.domain.factory.MessageFactory;

/**
 * @author Donald.Wang
 *
 */
class TestSenderManager {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		TestConfiguration config = new TestConfiguration();
		TestConfiguration.setUpBeforeClass();
		config.test();
	}

	@Test
	void test() throws Exception {
		MBService service = MSBaseApplication.getMessageBroker().getService("std-country");
		service.getSender().sendAsyncMessage(service.getTranslationMessage(MessageFactory.getRequestMessage(
				"{\"head\":{\"base\":{\"id\":\"fde703ae-5270-0ea5-bdfc-4ba13231cd67\",\"type\":0,\"sequence\":\"1/1\",\"expireTime\":0,\"timeStamp\":1539673602362},\"consumer\":{\"clientId\":\"StandingData\",\"id\":\"UI\"},\"services\":[{\"id\":\"eloan-accrue\",\"applicationId\":\"StandingData\",\"sync\":true,\"properties\":{\"actionType\":\"VIEW\",\"sqlType\":\"INQUIRE\"}}],\"trx\":{\"unitCode\":\"StandingData\",\"funcId\":\"COUNTRY\"}},\"body\":[{\"id\":\"eloan-accrue\",\"reqData\":{\"formData\":{\"\":\"\"},\"clauseData\":{\"C_CNTY_CODE\":\"AA\"}}}]}")));
		for (int i = 0; i <= 15; i++) {
			System.out.println(i);
			GetSender s = new GetSender();
			s.start();
		}
		Thread.sleep(10000);
	}

}

class GetSender extends Thread {

	@Override
	public void run() {
		try {
			Sender sender = MSBaseApplication.getMessageBroker().getService("std-country").getSender();
			Thread.sleep(100);
			sender.sendSyncMessage(null);
			sender.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
