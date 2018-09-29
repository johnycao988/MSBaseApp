/**
 * 
 */
package com.cs.baseapp.api.base;

import java.util.Map;

import com.cs.baseapp.api.base.Base;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class AppBaseFactory {

	private AppBaseFactory() {

	}

	public static Base buildBase(Map<String, String> baseConfig) throws BaseAppException {
		if (baseConfig == null || baseConfig.isEmpty()) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0003"));
		}
		return new BaseEntity(baseConfig);
	}

}
