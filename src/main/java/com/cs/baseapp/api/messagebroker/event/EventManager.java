/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.PropertiesUtils;

/**
 * @author Donald.Wang
 *
 */
public class EventManager {

	private Map<String, MSBaseAppEvent> events = new HashMap<>();
	private EventOperator operator = new DefaultEventOperator();

	@SuppressWarnings("unchecked")
	public EventManager(List<Map<String, Object>> configs) {
		for (Map<String, Object> config : configs) {
			this.events.put((String) config.get(ConfigConstant.NAME.getValue()),
					new MSBaseAppEvent((String) config.get(ConfigConstant.NAME.getValue()),
							(String) config.get(ConfigConstant.HANDLER_CLASS.getValue()),
							PropertiesUtils.convertMapToProperties(
									(Map<String, String>) config.get(ConfigConstant.PARAMETERS.getValue()))));
		}

	}

	public EventMessageBuilder getEventMessageBuilder() {
		return new EventMessageBuilder();
	}

	public EventOperator getEventOperator() {
		return this.operator;
	}

	public MSBaseAppEvent getEvent(String eventName) {
		return this.events.get(eventName);
	}

}
