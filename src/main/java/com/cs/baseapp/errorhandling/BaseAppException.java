/**
 * 
 */
package com.cs.baseapp.errorhandling;

import com.cs.log.common.logbean.LogException;
import com.cs.log.common.logbean.LogInfo;

/**
 * @author Donald.Wang
 *
 */
public class BaseAppException extends LogException {

	private static final long serialVersionUID = -4599015490145714637L;

	public BaseAppException(LogInfo logInfo) {
		super(logInfo);
	}

	public BaseAppException(Throwable th, LogInfo logInfo) {
		super(th, logInfo);
	}

}
