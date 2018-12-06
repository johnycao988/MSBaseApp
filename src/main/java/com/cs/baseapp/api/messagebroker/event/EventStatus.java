/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

/**
 * @author Donald.Wang
 *
 */
public class EventStatus {

	private EventStatus() {

	}

	public static final String EVENT_STATUS_ADD = "ADD";
	public static final String EVENT_STATUS_TRG_OK = "TRG-OK";
	public static final String EVENT_STATUS_TRG_ERR = "TRG-ERR";
	public static final String EVENT_STATUS_PROC_IN = "PROC-IN";
	public static final String EVENT_STATUS_PROC_ERR = "PROC-ERR";
	public static final String EVENT_STATUS_PROC_OK = "PROC-OK";

}
