/**
 * 
 */
package com.cs.cloud.message.api;

/**
 * @author Donald.Wang
 * 
 * @Description The <code>MessageHeadBase<code> is a part of Message
 *              header. It stores the base information of this message.
 *				
 */
public interface MessageHeadBase extends JsonElement {

	public String getId();

	public long getTimeStamp();

	public String getSequence();

	public String getCorrelationId();

	public boolean isExpire();

	public int getType();

}
