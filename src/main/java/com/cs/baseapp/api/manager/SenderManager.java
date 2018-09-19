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

import com.cs.baseapp.api.messagebroker.MessageSender;

/**
 * @author Donald.Wang
 *
 */
public class SenderManager {

	private Map<String, MessageSender> senders = new HashMap<>();

	public SenderManager(Map<String, MessageSender> senders) {
		this.senders = senders;
	}

	public MessageSender getById(String id) {
		return this.senders.get(id);
	}

	public List<MessageSender> getAll() {
		List<MessageSender> list = new ArrayList<>();
		Iterator<Entry<String, MessageSender>> it = this.senders.entrySet().iterator();
		while (it.hasNext()) {
			list.add(it.next().getValue());
		}
		return list;
	}

}
