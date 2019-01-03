/**
 * 
 */
package com.cs.cloud.message.api;

/**
 * @author Donald.Wang
 *
 */
public interface MessageHeadTransaction extends JsonElement {

	public String getReferenceNo();

	public String getTransactionNo();

	public String getUnitCode();

	public String getFunctionId();

}
