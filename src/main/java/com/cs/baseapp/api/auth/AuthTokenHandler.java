/**
 * 
 */
package com.cs.baseapp.api.auth;

import java.util.Properties;

import com.cs.baseapp.errorhandling.BaseAppException;

/**
 * @author Donald.Wang
 *
 */
public abstract class AuthTokenHandler {

	private String authTokenId;

	private Properties prop;

	private String currentToken;

	private boolean isDefault = false;

	public abstract String getTokenByFirst() throws BaseAppException;

	public abstract String refreshToken() throws BaseAppException;

	public AuthTokenHandler(String id, boolean isDefault, Properties prop) {
		this.authTokenId = id;
		this.prop = prop;
		this.isDefault = isDefault;
	}

	public String getProperty(String key) {
		return this.prop.getProperty(key);
	}

	public String getId() {
		return this.authTokenId;
	}

	public String getCurrentToken() {
		return this.currentToken;
	}

	public void setCurrentToken(String newToken) {
		this.currentToken = newToken;
	}

	public boolean isDefault() {
		return this.isDefault;
	}

}
