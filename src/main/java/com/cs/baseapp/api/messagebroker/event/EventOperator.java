/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public interface EventOperator {

	public void triggerEvent(String eventId) throws BaseAppException;

	public void onEvent(EventMessage msgEvnet);

	public void addEvent(EventMessage msgEvnet) throws BaseAppException;

}
