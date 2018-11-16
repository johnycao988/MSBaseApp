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
public abstract class RefreshTokenHandler {

	private String refreshTokenId;

	private Properties prop;

	private String currentToken;

	public abstract String getTokenByFirst() throws BaseAppException;

	public abstract String refreshToken() throws BaseAppException;

	public RefreshTokenHandler(String id, Properties prop) throws BaseAppException {
		this.refreshTokenId = id;
		this.prop = prop;
	}

	public String getProperty(String key) {
		return this.prop.getProperty(key);
	}

	public String getId() {
		return this.refreshTokenId;
	}

	public String getCurrentToken() {
		return this.currentToken;
	}

	public void setCurrentToken(String newToken) {
		this.currentToken = newToken;
	}

}
