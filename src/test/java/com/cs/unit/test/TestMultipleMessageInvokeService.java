/**
 * 
 */
package com.cs.unit.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.cloud.message.domain.factory.MessageFactory;

/**
 * @author Donald.Wang
 *
 */
class TestMultipleMessageInvokeService {

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
	void test() throws BaseAppException, MessageException {
		MessageRequest csReqMsg = MessageFactory.getRequestMessage(
				"{\"head\":{\"base\":{\"id\":\"fde703ae-5270-0ea5-bdfc-4ba13231cd67\",\"type\":0,\"sequence\":\"1/1\",\"expireTime\":0,\"timeStamp\":1539673602362},\"consumer\":{\"clientId\":\"StandingData\",\"id\":\"UI\"},\"services\":[{\"id\":\"eloan-accrue\",\"applicationId\":\"StandingData\",\"sync\":true,\"properties\":{\"actionType\":\"VIEW\",\"sqlType\":\"INQUIRE\"}},{\"id\":\"eloan-test\",\"applicationId\":\"StandingData\",\"sync\":true,\"properties\":{\"actionType\":\"VIEW\",\"sqlType\":\"INQUIRE\"}}],\"trx\":{\"unitCode\":\"StandingData\",\"funcId\":\"COUNTRY\"}},\"body\":[{\"id\":\"eloan-accrue\",\"reqData\":{\"formData\":{\"\":\"\"},\"clauseData\":{\"C_CNTY_CODE\":\"AA\"}}},{\"id\":\"eloan-test\",\"reqData\":{\"formData\":{\"\":\"\"},\"clauseData\":{\"C_CNTY_CODE\":\"AA\"}}}]}");
		MSBaseApplication.doWebFilters(csReqMsg, null, null);
	}

}
