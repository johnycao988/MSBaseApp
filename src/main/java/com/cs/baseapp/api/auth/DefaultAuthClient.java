/**
 * 
 */
package com.cs.baseapp.api.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Donald.Wang
 *
 */
public class DefaultAuthClient implements AuthClient {

	private boolean isAuth = true;

	private String clientId;

	private List<String> roles = new ArrayList<>();

	public DefaultAuthClient(String clientId, boolean isAuth, String[] roles) {
		this.isAuth = isAuth;
		this.clientId = clientId;
		if (roles.length > 0) {
			for (String r : roles) {
				this.roles.add(r);
			}
		}
	}

	@Override
	public boolean isAuth() {
		return this.isAuth;
	}

	@Override
	public List<String> getRoles() {
		return this.roles;
	}

	@Override
	public String getClientId() {
		return this.clientId;
	}

}
