/**
 * 
 */
package com.cs.unit.test;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.messagebroker.BaseMessageListener;

/**
 * @author Donald.Wang
 *
 */
class TestListenerManager {

	public static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

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
		try {
			BaseMessageListener listener = MSBaseApplication.getMessageBroker().getListener("TestListener");
			System.out.println(listener.getMaxProcessThreads());
			Thread.sleep(1000);
			for (int i = 0; i <= 100; i++) {
				queue.offer("" + i);
				// Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("End");
	}

}
