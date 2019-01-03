/**
 * 
 */
package com.cs.cloud.message.api;

/**
 * @author Donald.Wang
 *
 */
public interface MessageResponseBodyServiceStatus extends JsonElement {

	public boolean isSuccess();

	public String getErrCode();

	public String getErrMsg();
}
