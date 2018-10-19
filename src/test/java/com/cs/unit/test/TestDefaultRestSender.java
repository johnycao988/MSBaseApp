/**
 * 
 */
package com.cs.unit.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Donald.Wang
 *
 */
class TestDefaultRestSender {

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
	void test() {
		
	}

}
