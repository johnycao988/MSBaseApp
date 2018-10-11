/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.List;
import java.util.Properties;

import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;

/**
 * @author Donald.Wang
 *
 */
public abstract class MessageListener {

	protected String id;

	protected int maxProcessThreads;

	protected Properties prop;

	protected List<MessageFilter> filters;

	public MessageListener(String id, int maxProcessThreads, Properties prop, List<MessageFilter> filters) {
		this.id = id;
		this.maxProcessThreads = maxProcessThreads;
		this.prop = prop;
		this.filters = filters;
	}

	public abstract void initialize() throws BaseAppException;

	public abstract void start() throws BaseAppException;

	public abstract void stop() throws BaseAppException;

	public String getId() {
		return this.id;
	}

	public void doListenerFilter(MessageRequest reqMessage) throws BaseAppException {
		for (MessageFilter filter : this.filters) {
			filter.doListenerFilter(reqMessage);
		}
	}

	public int getMaxProcessThreads() {
		return this.maxProcessThreads;
	}

	public Properties getProperties() {
		return this.prop;
	}

	public String getProperty(String key) {
		return this.prop.getProperty(key);
	}

	public List<MessageFilter> getMessageFilters() {
		return this.filters;
	}

}
