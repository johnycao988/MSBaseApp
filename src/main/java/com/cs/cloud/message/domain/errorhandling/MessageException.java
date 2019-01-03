/**
 * 
 */
package com.cs.cloud.message.domain.errorhandling;

/**
 * @author Donald.Wang
 *
 */
public class MessageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7594222036824876370L;

	public MessageException(String errorMsg) {
		super(errorMsg);
	}

	public MessageException(String errorMsg, Throwable e) {
		super(errorMsg, e);
	}
}
