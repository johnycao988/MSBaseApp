/**
 * 
 */
package com.cs.baseapp.api.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.filter.BaseMessageFilter;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class DefaultOauth2OidcAuthMsgFilter extends BaseMessageFilter {

	Logger logger = LogManager.getSystemLog();

	public DefaultOauth2OidcAuthMsgFilter(String id, String urlPattern, Properties prop, String authRuleId) {
		super(id, urlPattern, prop, authRuleId);
	}

	@Override
	public void doWebFilter(MessageRequest csReqMsg, ServletRequest request, ServletResponse response)
			throws BaseAppException, MessageException {
		AuthRule authRule = super.getAuthRule();
		AuthClient ac = authRule.getAuthClient(csReqMsg.getConsumer().getClientId());
		if (ac == null) {
			ac = getDefaultAuthClient();
		}
		if (ac.isAuth()) {
			authenticationMessage(authRule, csReqMsg);
			authorizationMessage(authRule, csReqMsg);
		}
	}

	private void authenticationMessage(AuthRule rule, MessageRequest request) throws AuthException {
		HttpPost post = new HttpPost(rule.getProperty(ConfigConstant.USERINFO_ENDPOINT.getValue()));
		post.setHeader(AuthConst.REQUEST_CONTENT_TYPE, AuthConst.REQUEST_FORM_TYPE);
		CloseableHttpResponse resp = null;
		try (CloseableHttpClient client = HttpClients.createDefault();) {
			post.setEntity(EntityBuilder.create()
					.setParameters(
							new BasicNameValuePair(AuthConst.PARA_ACCESS_TOKEN, request.getConsumer().getToken()))
					.build());
			resp = client.execute(post);
		} catch (Exception e) {
			logger.write(LogManager.getServiceLogKey(request),
					new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0063", request.getJsonString())));
		}
		if (resp != null && 200 != resp.getStatusLine().getStatusCode()) {
			throw new AuthException(LogInfoMgr.getErrorInfo("ERR_0064", request.getJsonString()));
		}
	}

	private void authorizationMessage(AuthRule rule, MessageRequest request) throws BaseAppException {
		AuthClient authClient = rule.getAuthClient(request.getConsumer().getClientId());
		List<String> needCheckRoles = authClient.getRoles();
		Map<String, Object> tokenInfo = parseAccessTokenByJWT(MSBaseApplication.getBaseInfo().getAppId(),
				request.getConsumer().getToken());
		if (needCheckRoles.contains(AuthConst.ROLE_USER) && !checkUser(tokenInfo, request)) {
			throw new AuthException(LogInfoMgr.getErrorInfo("ERR_0066", request.getJsonString()));
		}
		if (needCheckRoles.contains(AuthConst.ROLE_SERVICE) && !checkService(tokenInfo, request)) {
			throw new AuthException(LogInfoMgr.getErrorInfo("ERR_0067", request.getJsonString()));
		}
		if (needCheckRoles.contains(AuthConst.ROLE_FUNC) && !checkFunc(tokenInfo, request)) {
			throw new AuthException(LogInfoMgr.getErrorInfo("ERR_0068", request.getJsonString()));
		}
		if (needCheckRoles.contains(AuthConst.ROLE_ORG_UNIT) && checkOrgUnit(tokenInfo, request)) {
			throw new AuthException(LogInfoMgr.getErrorInfo("ERR_0069", request.getJsonString()));
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> parseAccessTokenByJWT(String clientId, String token) {
		Map<String, Object> tokenInfo = new HashMap<>();
		DecodedJWT jwt = JWT.decode(token);
		tokenInfo.put(AuthConst.ROLE_USER, jwt.getClaim("name").asString());
		List<String> roles = (List<String>) ((Map<String, Object>) jwt.getClaim("resource_access").asMap()
				.get(clientId)).get("roles");
		Map<String, String> funcRolesMap = new HashMap<>();
		Map<String, String> orgUnitRolesMap = new HashMap<>();
		Map<String, String> serviceRolesMap = new HashMap<>();
		for (String role : roles) {
			String[] roleArray = role.trim().split(":");
			switch (roleArray[0]) {
			case AuthConst.ROLE_FUNC:
				funcRolesMap.put(roleArray[2], roleArray[2]);
				break;
			case AuthConst.ROLE_ORG_UNIT:
				orgUnitRolesMap.put(roleArray[1], roleArray[1]);
				break;
			case AuthConst.ROLE_SERVICE:
				serviceRolesMap.put(roleArray[1], roleArray[1]);
				break;
			default:
				break;
			}
		}
		tokenInfo.put(AuthConst.ROLE_FUNC, funcRolesMap);
		tokenInfo.put(AuthConst.ROLE_ORG_UNIT, orgUnitRolesMap);
		tokenInfo.put(AuthConst.ROLE_SERVICE, serviceRolesMap);
		return tokenInfo;
	}

	private AuthClient getDefaultAuthClient() {
		return new DefaultAuthClient("Default_Auth_Client", true, new String[] { AuthConst.ROLE_SERVICE });
	}

	private boolean checkUser(Map<String, Object> tokenInfo, MessageRequest request) {
		return ((String) tokenInfo.get(AuthConst.ROLE_USER)).equals(request.getConsumer().getUserId());
	}

	@SuppressWarnings("unchecked")
	private boolean checkOrgUnit(Map<String, Object> tokenInfo, MessageRequest request) {
		return ((Map<String, String>) tokenInfo.get(AuthConst.ROLE_ORG_UNIT))
				.containsKey(request.getTransaction().getUnitCode());
	}

	@SuppressWarnings("unchecked")
	private boolean checkFunc(Map<String, Object> tokenInfo, MessageRequest request) {
		return ((Map<String, String>) tokenInfo.get(AuthConst.ROLE_FUNC))
				.containsKey(request.getTransaction().getFunctionId());
	}

	@SuppressWarnings("unchecked")
	private boolean checkService(Map<String, Object> tokenInfo, MessageRequest request) {
		boolean isPass = true;
		List<MessageHeadService> services = request.getServices();
		for (MessageHeadService s : services) {
			isPass = isPass && ((Map<String, String>) tokenInfo.get(AuthConst.ROLE_SERVICE)).containsKey(s.getId());
		}
		return isPass;
	}

	@Override
	public void doListenerFilter(MessageRequest requestMsg) throws BaseAppException {
		try {
			doWebFilter(requestMsg, null, null);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doListenerEventFilter(TranslationMessage message) throws BaseAppException {
		// do nothing

	}

}
