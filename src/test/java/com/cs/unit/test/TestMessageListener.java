/**
 * 
 */
package com.cs.unit.test;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.api.manager.ListenerManager;
import com.cs.baseapp.api.messagebroker.BaseMessageListener;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public class TestMessageListener extends BaseMessageListener {

	public TestMessageListener(String id, int maxProcessThreads, Properties prop, List<MessageFilter> filters,
			int connections, String tranformClass) {
		super(id, maxProcessThreads, prop, filters, connections, tranformClass);
	}

	@Override
	public void initialize() throws BaseAppException {
		System.out.println("Listener Init!");
	}

	@Override
	public void start() throws BaseAppException {
		System.out.println("Listener start!");
		Listener l = new Listener(super.getId());
		l.start();
	}

	@Override
	public void stop() throws BaseAppException {
		System.out.println("Listener stop!");
	}

	@Override
	public Object receive() throws BaseAppException {
		return null;
	}

}

class Listener extends Thread {

	String id;

	public Listener(String id) {
		this.id = id;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String m = TestListenerManager.queue.poll();
				if (!StringUtils.isEmpty(m)) {
					System.out.println(Thread.currentThread().getName() + " : Listener get Message: " + m);
					ListenerManager.doMessage(this.id, new Task(m));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class Task implements Runnable {

	private String s;

	public Task(String s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " : Process Message! MessageContent:" + s);
	}

}
