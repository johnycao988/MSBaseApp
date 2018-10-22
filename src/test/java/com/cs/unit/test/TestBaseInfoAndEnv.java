/**
 * 
 */
package com.cs.unit.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
class TestBaseInfoAndEnv {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		TestConfiguration config = new TestConfiguration();
		TestConfiguration.setUpBeforeClass();
		config.test();
	}

	@Test
	void test() throws BaseAppException {
		String appId = MSBaseApplication.getBaseInfo().getAppId();
		String version = MSBaseApplication.getBaseInfo().getVersion();
		String name = MSBaseApplication.getBaseInfo().getName();
		String secret = MSBaseApplication.getBaseInfo().getSecret();
		assertEquals(appId, "eloan");
		assertEquals(version, "v01");
		assertEquals(name, "eloanMS");
		assertEquals(secret, "secret");
	}

}
