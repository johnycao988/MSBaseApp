/**
 * 
 */
package com.cs.baseapp.api.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Donald.Wang
 *
 */
public class DefaultAuthRule implements AuthRule {

	private String id;

	private String refreshTokenId;

	private Properties prop;

	private Map<String, AuthClient> authClients = new HashMap<>();

	public DefaultAuthRule(String id, String refreshTokenId, Properties prop, List<AuthClient> authClients) {
		this.id = id;
		this.refreshTokenId = refreshTokenId;
		this.prop = prop;
		for (AuthClient ac : authClients) {
			this.authClients.put(ac.getClientId(), ac);
		}

	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getRefreshTokenId() {
		return this.refreshTokenId;
	}

	@Override
	public String getProperty(String key) {
		return this.prop.getProperty(key);
	}

	@Override
	public AuthClient getAuthClient(String clientId) {
		return this.authClients.get(clientId);
	}

	@Override
	public List<AuthClient> getAuthClients() {
		List<AuthClient> rs = new ArrayList<>();
		rs.addAll(this.authClients.values());
		return rs;
	}

}
