/**
 * 
 */
package com.cs.baseapp.api.appenv;

import java.util.Map;
import java.util.Properties;

import com.cs.baseapp.utils.PropertiesUtils;

/**
 * @author Donald.Wang
 *
 */
public class AppEnvEntity implements AppEnv {

	private Properties propEnv = new Properties();

	public AppEnvEntity(Map<String, String> config) {
		this.propEnv = PropertiesUtils.convertMapToProperties(config);
	}

	@Override
	public String getEnvProperty(String key) {
		return this.propEnv.getProperty(key);
	}

}
