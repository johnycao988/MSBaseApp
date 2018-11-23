/**
 * 
 */
package com.cs.baseapp.api.auth;

/**
 * @author Donald.Wang
 *
 */
public class AuthConst {

	private AuthConst() {
	}

	public static final String ROLE_USER = "user";
	
	public static final String ROLE_FUNC = "func";
	
	public static final String ROLE_SERVICE = "service";
	
	public static final String ROLE_ORG_UNIT = "orgUnit";

	public static final String REQUEST_CONTENT_TYPE = "Content-Type";

	public static final String REQUEST_FORM_TYPE = "application/x-www-form-urlencoded";

	public static final String PARA_CLIENT_ID = "client_id";

	public static final String PARA_CLIENT_SECRET = "client_secret";

	public static final String PARA_TAKEN_ENDPOINT = "token_endpoint";

	public static final String PARA_GRANT_TYPE = "grant_type";

	public static final String PARA_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

	public static final String PARA_GRANT_TYPE_CLIENT_CREDNETIALS = "client_credentials";

	public static final String PARA_ACCESS_TOKEN = "access_token";

	public static final String PARA_EXPIRES_IN = "expires_in";

}
