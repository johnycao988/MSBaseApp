/**
 * 
 */
package com.cs.baseapp.api.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cs.baseapp.api.messagebroker.MessageListener;

/**
 * @author Donald.Wang
 *
 */
public class ListenerManager implements Repository<MessageListener> {

	private Map<String, MessageListener> listeners = new HashMap<>();

	public ListenerManager(Map<String, MessageListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public MessageListener getById(String id) {
		return listeners.get(id);
	}

	@Override
	public List<MessageListener> getAll() {
		List<MessageListener> list = new ArrayList<>();
		Iterator<Entry<String, MessageListener>> it = this.listeners.entrySet().iterator();
		while (it.hasNext()) {
			list.add(it.next().getValue());
		}
		return list;
	}

}
