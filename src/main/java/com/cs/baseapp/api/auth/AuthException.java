/**
 * 
 */
package com.cs.baseapp.api.auth;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.log.common.logbean.LogInfo;

/**
 * @author Donald.Wang
 *
 */
public class AuthException extends BaseAppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthException(LogInfo logInfo) {
		super(logInfo);
	}

}
