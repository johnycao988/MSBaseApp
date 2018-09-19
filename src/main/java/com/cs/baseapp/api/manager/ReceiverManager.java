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

import com.cs.baseapp.api.messagebroker.MessageReceiver;

/**
 * @author Donald.Wang
 *
 */
public class ReceiverManager {

	private Map<String, MessageReceiver> receivers = new HashMap<>();

	public ReceiverManager(Map<String, MessageReceiver> receivers) {
		this.receivers = receivers;
	}

	public MessageReceiver getById(String id) {
		return receivers.get(id);
	}

	public List<MessageReceiver> getAll() {
		List<MessageReceiver> list = new ArrayList<>();
		Iterator<Entry<String, MessageReceiver>> it = this.receivers.entrySet().iterator();
		while (it.hasNext()) {
			list.add(it.next().getValue());
		}
		return list;
	}

}
