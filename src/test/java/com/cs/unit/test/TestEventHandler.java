/**
 * 
 */
package com.cs.unit.test;

import com.cs.baseapp.api.messagebroker.event.EventHandler;
import com.cs.baseapp.api.messagebroker.event.EventMessage;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public class TestEventHandler implements EventHandler {

	@Override
	public void onEvent(EventMessage eventMsg) throws BaseAppException {
		System.out.println("Process " + eventMsg.getJsonString());
	}

}
