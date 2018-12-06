package com.cs.baseapp.api.messagebroker.event;

import java.util.Properties;

public class MSBaseAppEvent {

	private String name;

	private Properties prop = null;

	private String handlerClass;

	public MSBaseAppEvent(String name, String handlerClass, Properties prop) {
		this.name = name;
		this.handlerClass = handlerClass;
		this.prop = prop;
	}

	public String getProperty(String key) {
		return this.prop.getProperty(key);
	}

	public String getName() {
		return this.name;
	}

	public EventHandler getEventHandler() {
		try {
			return (EventHandler) Class.forName(this.handlerClass).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
