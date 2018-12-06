/**
 * 
 */
package com.cs.unit.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
class TestBaseAppShutdown {

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
	void test() throws BaseAppException {
		int s = MSBaseApplication.getMessageBroker().getListener("TestListener").getMaxProcessThreads();
		System.out.println(s);
		MSBaseApplication.shutdown();
		// MSBaseApplication.doWebFilters(null, null, null);
		// MSBaseApplication.getAppEnv().getEnvProperty("OPOP");
		// MSBaseApplication.getBaseInfo().getAppId();
		int n = MSBaseApplication.getMessageBroker().getListener("TestListener").getMaxProcessThreads();
		System.out.println(n);
	}

}
