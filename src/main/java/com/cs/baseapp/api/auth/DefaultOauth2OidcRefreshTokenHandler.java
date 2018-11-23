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
import com.cs.baseapp.logger.LogManager;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class DefaultOauth2OidcRefreshTokenHandler extends AuthTokenHandler {

	private Logger logger = LogManager.getSystemLog();

	public DefaultOauth2OidcRefreshTokenHandler(String id, boolean isDefault, Properties prop) throws BaseAppException {
		super(id, isDefault, prop);
	}

	private String refreshToken = "";

	@Override
	public String refreshToken() throws BaseAppException {
		logger.debug(LogManager.getServiceLogKey(), "Start to refresh application token. AuthTokenId:" + super.getId());
		HttpPost post = new HttpPost(super.getProperty(AuthConst.PARA_TAKEN_ENDPOINT));
		post.setHeader(AuthConst.REQUEST_CONTENT_TYPE, AuthConst.REQUEST_FORM_TYPE);
		CloseableHttpResponse resp = null;
		try (CloseableHttpClient client = HttpClients.createDefault();) {
			EntityBuilder entityBuilder = EntityBuilder.create();
			entityBuilder.setParameters(
					new BasicNameValuePair(AuthConst.PARA_GRANT_TYPE, AuthConst.PARA_GRANT_TYPE_REFRESH_TOKEN),
					new BasicNameValuePair(AuthConst.PARA_CLIENT_ID, MSBaseApplication.getBaseInfo().getAppId()),
					new BasicNameValuePair(AuthConst.PARA_CLIENT_SECRET, MSBaseApplication.getBaseInfo().getSecret()),
					new BasicNameValuePair(AuthConst.PARA_GRANT_TYPE_REFRESH_TOKEN, this.refreshToken));
			post.setEntity(entityBuilder.build());
			resp = client.execute(post);
			JsonNode jsonObj = new ObjectMapper()
					.readTree(EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8")));
			String accToken = jsonObj.path(AuthConst.PARA_ACCESS_TOKEN).asText();
			this.refreshToken = jsonObj.path(AuthConst.PARA_GRANT_TYPE_REFRESH_TOKEN).asText();
			super.setExpireTime(jsonObj.path(AuthConst.PARA_EXPIRES_IN).asInt());
			super.setCurrentToken(accToken);
			logger.debug(LogManager.getServiceLogKey(),
					"Refresh appliaction token success! AuthTokenId: " + super.getId() + ", Token: " + accToken);
		} catch (Exception e) {
			logger.write(LogManager.getServiceLogKey(),
					new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0062", super.getId())));
		}
		return super.getCurrentToken();
	}

	@Override
	public String getTokenByFirst() throws BaseAppException {
		logger.info(LogManager.getServiceLogKey(),
				"Start to register the application to sso service. AuthTokenId:" + super.getId());
		HttpPost post = new HttpPost(super.getProperty(AuthConst.PARA_TAKEN_ENDPOINT));
		post.setHeader(AuthConst.REQUEST_CONTENT_TYPE, AuthConst.REQUEST_FORM_TYPE);
		CloseableHttpResponse resp = null;
		try (CloseableHttpClient client = HttpClients.createDefault();) {
			EntityBuilder entityBuilder = EntityBuilder.create();
			entityBuilder.setParameters(
					new BasicNameValuePair(AuthConst.PARA_GRANT_TYPE, AuthConst.PARA_GRANT_TYPE_CLIENT_CREDNETIALS),
					new BasicNameValuePair(AuthConst.PARA_CLIENT_ID, MSBaseApplication.getBaseInfo().getAppId()),
					new BasicNameValuePair(AuthConst.PARA_CLIENT_SECRET, MSBaseApplication.getBaseInfo().getSecret()));
			post.setEntity(entityBuilder.build());
			resp = client.execute(post);
			JsonNode jsonObj = new ObjectMapper()
					.readTree(EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8")));
			String accToken = jsonObj.path(AuthConst.PARA_ACCESS_TOKEN).asText();
			this.refreshToken = jsonObj.path(AuthConst.PARA_GRANT_TYPE_REFRESH_TOKEN).asText();
			super.setExpireTime(jsonObj.path(AuthConst.PARA_EXPIRES_IN).asInt());
			super.setCurrentToken(accToken);
			logger.info(LogManager.getServiceLogKey(),
					"Register the application to sso service Success! AuthTokenId:" + super.getId());
		} catch (Exception e) {
			logger.write(LogManager.getServiceLogKey(),
					new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0070", super.getId())));
		}
		return super.getCurrentToken();
	}

}
