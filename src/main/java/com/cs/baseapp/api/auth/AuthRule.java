/**
 * 
 */
package com.cs.baseapp.api.auth;

import java.util.List;

/**
 * @author Donald.Wang
 *
 */
public interface AuthRule {

	public String getId();

	public String getRefreshTokenId();

	public String getProperty(String key);

	public AuthClient getAuthClient(String clientId);

	public List<AuthClient> getAuthClients();

}
