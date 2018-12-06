/**
 * 
 */
package com.cs.baseapp.api.messagebroker.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.manager.ListenerManager;
import com.cs.baseapp.api.messagebroker.BaseMessageListener;
import com.cs.baseapp.api.messagebroker.entity.MessageProcessTask;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public class MSBaseAppListenerThread {

	private ListenerHandlerThread listenerThread;
	private ExecutorService singleThreadPool;
	private BaseMessageListener listener;

	public MSBaseAppListenerThread(BaseMessageListener listener) {
		this.listenerThread = new ListenerHandlerThread(listener);
		this.listener = listener;
	}

	public void start() throws BaseAppException {
		this.singleThreadPool = Executors.newSingleThreadExecutor();
		this.listenerThread.start();
		this.singleThreadPool.execute(listenerThread);
	}

	public void stop() throws BaseAppException {
		this.listenerThread.stop();
		this.singleThreadPool.shutdown();
	}

	public BaseMessageListener getListener() {
		return this.listener;
	}

}

class ListenerHandlerThread implements Runnable {

	private BaseMessageListener listener;

	public ListenerHandlerThread(BaseMessageListener listener) {
		this.listener = listener;
	}

	public void start() throws BaseAppException {
		this.listener.initialize();
		this.listener.start();
	}

	public void stop() throws BaseAppException {
		this.listener.stop();
	}

	@Override
	public void run() {
		if (this.listener != null) {
			try {
				while (!Thread.interrupted()) {
					MessageProcessTask task = ListenerManager.getMessageTaskObject(this.listener.getId());
					Object message = this.listener.receive();
					if (message != null) {
						task.setMessage(this.listener.getId(), this.listener.getTranslationMessage(message),
								this.listener.getMessageFilters());
						ListenerManager.doMessage(this.listener.getId(), task);
					} else {
						System.out.println(Thread.currentThread().getName() + ": Receive no message wait 2 seconds!");
						ListenerManager.releaseMessageTaskObject(this.listener.getId(), task);
						Thread.sleep(100);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
