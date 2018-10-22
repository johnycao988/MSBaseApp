/**
 * 
 */
package com.cs.baseapp.utils;

/**
 * @author Donald.Wang
 *
 */
public enum MSBaseAppStatus {

	RUNNING(0), STOPPING(1), STOPED(2);

	private int status = 0;

	MSBaseAppStatus(int status) {
		this.status = status;
	}

	public int getValue() {
		return this.status;
	}

}
