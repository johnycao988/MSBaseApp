/**
 * 
 */
package com.cs.baseapp.api.messagebroker;

/**
 * @author Donald.Wang
 *
 */
public interface ConnectionPoolManager {

	public void init();

	public Object getConnection();

	public Object getConnection(long timeout);

}
