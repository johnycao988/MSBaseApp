/**
 * 
 */
package com.cs.baseapp.api.base;

import java.util.Map;

import com.cs.baseapp.api.base.Base;

/**
 * @author Donald.Wang
 *
 */
public class AppBaseFactory {

	private AppBaseFactory() {

	}

	public static Base buildBase(Map<String, String> baseConfig) {
		return new BaseEntity(baseConfig);
	}

}
