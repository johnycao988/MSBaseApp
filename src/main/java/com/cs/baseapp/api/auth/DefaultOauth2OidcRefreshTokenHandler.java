/**
 * 
 */
package com.cs.baseapp.api.auth;

import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class DefaultOauth2OidcRefreshTokenHandler extends AuthTokenHandler {

	public DefaultOauth2OidcRefreshTokenHandler(String id, boolean isDefault, Properties prop) throws BaseAppException {
		super(id, isDefault, prop);
	}

	private String refreshToken = "";

	@Override
	public String refreshToken() throws BaseAppException {
		HttpPost post = new HttpPost(super.getProperty("token_endpoint"));
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		CloseableHttpResponse resp = null;
		try (CloseableHttpClient client = HttpClients.createDefault();) {
			EntityBuilder entityBuilder = EntityBuilder.create();
			entityBuilder.setParameters(new BasicNameValuePair("grant_type", "refresh_token"),
					new BasicNameValuePair("client_id", MSBaseApplication.getBaseInfo().getAppId()),
					new BasicNameValuePair("client_secret", MSBaseApplication.getBaseInfo().getSecret()),
					new BasicNameValuePair("refresh_token", this.refreshToken));
			post.setEntity(entityBuilder.build());
			resp = client.execute(post);
			JsonNode jsonObj = new ObjectMapper()
					.readTree(EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8")));
			String accToken = jsonObj.path("access_token").asText();
			this.refreshToken = jsonObj.path("refresh_token").asText();
			super.setCurrentToken(accToken);
			System.out.println("Refresh Token:" + super.getCurrentToken());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getCurrentToken();
	}

	@Override
	public String getTokenByFirst() throws BaseAppException {
		HttpPost post = new HttpPost(super.getProperty("token_endpoint"));
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		CloseableHttpResponse resp = null;
		try (CloseableHttpClient client = HttpClients.createDefault();) {
			EntityBuilder entityBuilder = EntityBuilder.create();
			entityBuilder.setParameters(new BasicNameValuePair("grant_type", "client_credentials"),
					new BasicNameValuePair("client_id", MSBaseApplication.getBaseInfo().getAppId()),
					new BasicNameValuePair("client_secret", MSBaseApplication.getBaseInfo().getSecret()));
			post.setEntity(entityBuilder.build());
			resp = client.execute(post);
			JsonNode jsonObj = new ObjectMapper()
					.readTree(EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8")));
			String accToken = jsonObj.path("access_token").asText();
			this.refreshToken = jsonObj.path("refresh_token").asText();
			super.setCurrentToken(accToken);
			System.out.println("Get Token First:" + super.getCurrentToken());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getCurrentToken();
	}

}
