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

import com.cs.baseapp.api.messagebroker.MBService;

/**
 * @author Donald.Wang
 *
 */
public class ServiceRepository implements Repository<MBService> {
	private Map<String, MBService> services = new HashMap<>();

	public ServiceRepository(Map<String, MBService> services) {
		this.services = services;
	}

	@Override
	public MBService getById(String id) {
		return this.services.get(id);
	}

	@Override
	public List<MBService> getAll() {
		List<MBService> list = new ArrayList<>();
		Iterator<Entry<String, MBService>> it = this.services.entrySet().iterator();
		while (it.hasNext()) {
			list.add(it.next().getValue());
		}
		return list;
	}

}
