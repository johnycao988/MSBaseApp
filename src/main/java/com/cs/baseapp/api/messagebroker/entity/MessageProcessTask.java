/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.util.ArrayList;
import java.util.List;

import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.api.manager.ListenerManager;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public class MessageProcessTask implements Runnable {

	private TranslationMessage message;

	private List<MessageFilter> filters = new ArrayList<>();

	private String listenerId = "";

	public void setMessage(String listenerId, TranslationMessage message, List<MessageFilter> filters) {
		this.message = message;
		this.filters = filters;
		this.listenerId = listenerId;
	}

	public void clear() {
		this.message = null;
		this.filters = new ArrayList<>();
		this.listenerId = "";
	}

	@Override
	public void run() {
		if (this.message != null && this.filters != null) {
			for (MessageFilter f : this.filters) {
				try {
					f.doListenerEventFilter(message);
					ListenerManager.releaseMessageTaskObject(this.listenerId, this);
				} catch (BaseAppException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
