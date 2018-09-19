/**
 * 
 */
package com.cs.baseapp.api.base;

import java.util.Map;

/**
 * @author Donald.Wang
 *
 */
public class BaseEntity implements Base {

	private String name;

	private String version;

	private String applicationId;

	private String secret;

	public BaseEntity(Map<String, String> baseConfig) {
		this.version = baseConfig.get("version");
		this.applicationId = baseConfig.get("applicationId");
		this.secret = baseConfig.get("secret");
		this.name = baseConfig.get("name");
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

}
