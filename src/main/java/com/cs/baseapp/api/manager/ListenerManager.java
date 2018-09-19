/**
 * 
 */
package com.cs.baseapp.api.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cs.baseapp.api.messagebroker.MessageListener;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public class ListenerManager {

	private Map<String, MessageListener> listeners = new HashMap<>();

	public ListenerManager(Map<String, MessageListener> listeners) {
		this.listeners = listeners;
	}

	public MessageListener getById(String id) {
		return listeners.get(id);
	}

	public List<MessageListener> getAll() {
		List<MessageListener> list = new ArrayList<>();
		Iterator<Entry<String, MessageListener>> it = this.listeners.entrySet().iterator();
		while (it.hasNext()) {
			list.add(it.next().getValue());
		}
		return list;
	}

	public void stop(String id) throws BaseAppException {
		this.listeners.get(id).stop();
	}

}
