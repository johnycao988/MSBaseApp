/**
 * 
 */
package com.cs.baseapp.api.auth;

import java.util.List;

/**
 * @author Donald.Wang
 *
 */
public interface AuthClient {

	public boolean isAuth();

	public List<String> getRoles();

	public String getClientId();

}
