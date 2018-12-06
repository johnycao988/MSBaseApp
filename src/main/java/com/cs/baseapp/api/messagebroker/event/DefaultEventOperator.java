/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public class DefaultEventOperator implements EventOperator {

	@Override
	public void triggerEvent(String eventId) throws BaseAppException {
		// get EventMessage
		try {
			onEvent(null);
			updateStatus(eventId, EventStatus.EVENT_STATUS_TRG_OK, "");
		} catch (Exception e) {
			updateStatus(eventId, EventStatus.EVENT_STATUS_TRG_ERR, "");
		}

	}

	@Override
	public void onEvent(EventMessage msgEvnet) {
		try {
			System.out.println("OnEvent:" + msgEvnet.getJsonString());
			updateStatus(msgEvnet.getEventId(), EventStatus.EVENT_STATUS_PROC_IN, "In Process");
			MSBaseAppEvent event = MSBaseApplication.getMessageBroker().getEventManager()
					.getEvent(msgEvnet.getEventName());
			event.getEventHandler().onEvent(msgEvnet);
			updateStatus(msgEvnet.getEventId(), EventStatus.EVENT_STATUS_PROC_OK, "Process OK");
		} catch (BaseAppException e) {
			updateStatus(msgEvnet.getEventId(), EventStatus.EVENT_STATUS_PROC_ERR, "Process Error");
			e.printStackTrace();
		}
	}

	@Override
	public void addEvent(EventMessage msgEvnet) throws BaseAppException {
		System.out.println("add event:" + msgEvnet.getJsonString());
	}

	private void updateStatus(String eventId, String processStatus, String processInfo) {
		System.out.println("******" + eventId + "," + processStatus + "," + processInfo + "******");
	}

}
