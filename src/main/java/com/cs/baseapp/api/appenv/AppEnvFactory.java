/**
 * 
 */
package com.cs.baseapp.api.appenv;

import java.util.Map;

/**
 * @author Donald.Wang
 *
 */
public class AppEnvFactory {

	private AppEnvFactory() {

	}

	public static AppEnv build(Map<String, String> config) {
		return new AppEnvEntity(config);
	}

}
