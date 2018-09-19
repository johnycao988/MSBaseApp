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

import com.cs.baseapp.api.messagebroker.MessageReceiver;

/**
 * @author Donald.Wang
 *
 */
public class ReceiverRepository implements Repository<MessageReceiver> {

	private Map<String, MessageReceiver> receivers = new HashMap<>();

	public ReceiverRepository(Map<String, MessageReceiver> receivers) {
		this.receivers = receivers;
	}

	@Override
	public MessageReceiver getById(String id) {
		return receivers.get(id);
	}

	@Override
	public List<MessageReceiver> getAll() {
		List<MessageReceiver> list = new ArrayList<>();
		Iterator<Entry<String, MessageReceiver>> it = this.receivers.entrySet().iterator();
		while (it.hasNext()) {
			list.add(it.next().getValue());
		}
		return list;
	}

}
