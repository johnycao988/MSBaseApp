/**
 * 
 */
package com.cs.baseapp.api.base;

import java.util.Map;

import com.cs.baseapp.utils.ConfigConstant;

/**
 * @author Donald.Wang
 *
 */
public class BaseEntity implements Base {

	private String name;

	private String version;

	private String applicationId;

	private String secret;

	private String systemDs;

	public BaseEntity(Map<String, String> baseConfig) {
		this.version = baseConfig.get(ConfigConstant.BASE_VERSION.getValue());
		this.applicationId = baseConfig.get(ConfigConstant.BASE_APP_ID.getValue());
		this.secret = baseConfig.get(ConfigConstant.BASE_SECRET.getValue());
		this.name = baseConfig.get(ConfigConstant.BASE_NAME.getValue());
		this.systemDs = baseConfig.get(ConfigConstant.SYSTEM_DATA_SOURCE.getValue());
	}

	@Override
	public String getVersion() {
		return this.version;
	}

	@Override
	public String getAppId() {
		return this.applicationId;
	}

	@Override
	public String getSecret() {
		return this.secret;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getSystemDataSource() {
		return this.systemDs;
	}

}
