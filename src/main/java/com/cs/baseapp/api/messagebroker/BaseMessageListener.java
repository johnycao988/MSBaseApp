/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public abstract class BaseMessageListener {

	protected String id;

	protected int maxProcessThreads;

	protected Properties prop;

	protected List<MessageFilter> filters;

	protected int connections;

	protected String tranformClass;

	protected ExecutorService threadPool;
	
	public BaseMessageListener(String id, int maxProcessThreads, Properties prop, List<MessageFilter> filters,
			int connections, String tranformClass) {
		this.id = id;
		this.maxProcessThreads = maxProcessThreads;
		this.prop = prop;
		this.filters = filters;
		this.connections = connections;
		this.tranformClass = tranformClass;
		this.threadPool = Executors.newFixedThreadPool(this.maxProcessThreads);
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

	public int getConnections() {
		return this.connections;
	}

	public TranslationMessage getTranslationMessage(MessageRequest req) throws BaseAppException {
		Object instance = null;
		try {
			instance = Class.forName(this.tranformClass).getConstructor(Properties.class, MessageRequest.class)
					.newInstance(this.prop, req);
		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0027", this.id, this.tranformClass, req.getJsonString()));
		}
		return (TranslationMessage) instance;
	}

	public void doMessage(Runnable exec) {
		this.threadPool.execute(exec);
	}

}
