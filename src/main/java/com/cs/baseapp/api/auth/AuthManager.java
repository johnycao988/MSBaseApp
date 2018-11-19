/**
 * 
 */
package com.cs.baseapp.api.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class AuthManager {

	private Map<String, AuthRule> authRules = new HashMap<>();

	private Map<String, AuthTokenHandler> authTokens = new HashMap<>();

	private String defaultAuthTokenId;

	private ExecutorService threadPool;

	private Logger logger = LogManager.getSystemLog();

	@SuppressWarnings("unchecked")
	public AuthManager(Map<String, Object> configs) {
		buildAuthTokenHandlers((List<Map<String, Object>>) configs.get(ConfigConstant.AUTH_TOKEN.getValue()));
		buildAuthRules((List<Map<String, Object>>) configs.get(ConfigConstant.RULES.getValue()));
	}

	public void start() {
		this.threadPool = Executors.newFixedThreadPool(this.authRules.size());
		Set<Entry<String, AuthTokenHandler>> set = this.authTokens.entrySet();
		for (Entry<String, AuthTokenHandler> e : set) {
			try {
				this.threadPool.execute(new RefreshTokenTask(e.getValue()));
			} catch (BaseAppException e1) {
				e1.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void buildAuthRules(List<Map<String, Object>> authRuleConfig) {

		String id = null;
		for (Map<String, Object> c : authRuleConfig) {
			try {
				id = (String) c.get(ConfigConstant.ID.getValue());
				String authTokenId = (String) c.get(ConfigConstant.AUTH_TOKEN_ID.getValue());
				Properties prop = PropertiesUtils
						.convertMapToProperties((Map<String, String>) c.get(ConfigConstant.PARAMETERS.getValue()));
				List<Map<String, Object>> authClientConfig = (List<Map<String, Object>>) c
						.get(ConfigConstant.CLIENTS.getValue());
				List<AuthClient> authClients = new ArrayList<>();
				for (Map<String, Object> ac : authClientConfig) {
					authClients.add(new DefaultAuthClient((String) ac.get(ConfigConstant.CLIENT_ID.getValue()),
							(boolean) ac.get(ConfigConstant.IS_AUTH.getValue()),
							((String) ac.get(ConfigConstant.ROLES.getValue())).trim().split(",")));
				}
				this.authRules.put(id, new DefaultAuthRule(id, authTokenId, prop, authClients));
			} catch (Exception e) {
				logger.write(LogManager.getServiceLogKey(), new BaseAppException(e, LogInfoMgr.getErrorInfo("", id)));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void buildAuthTokenHandlers(List<Map<String, Object>> refreshTokenConfig) {
		for (Map<String, Object> c : refreshTokenConfig) {
			String authTokenId = null;
			String implClass = null;
			try {
				authTokenId = (String) c.get(ConfigConstant.ID.getValue());
				implClass = (String) c.get(ConfigConstant.IMPL_CLASS.getValue());
				Properties prop = PropertiesUtils
						.convertMapToProperties((Map<String, String>) c.get(ConfigConstant.PARAMETERS.getValue()));
				AuthTokenHandler headler = (AuthTokenHandler) Class.forName(implClass)
						.getConstructor(String.class, boolean.class, Properties.class)
						.newInstance(authTokenId, c.get(ConfigConstant.IS_DEFAULT.getValue()), prop);
				this.authTokens.put(headler.getId(), headler);
				if (headler.isDefault()) {
					this.defaultAuthTokenId = headler.getId();
				}
			} catch (Exception e) {
				logger.write(LogManager.getServiceLogKey(),
						new BaseAppException(e, LogInfoMgr.getErrorInfo("", authTokenId, implClass)));
			}
		}
	}

	public AuthRule getAuthRule(String authRuleId) {
		return this.authRules.get(authRuleId);
	}

	public String getAccessToken(String authTokenId) {
		return this.authTokens.get(authTokenId).getCurrentToken();
	}

	public String getDefaultAccessToken() {
		return this.getAccessToken(this.defaultAuthTokenId);
	}

	public String getAccessTokenByService(String serviceId) throws BaseAppException {
		String authTokenId = MSBaseApplication.getMessageBroker().getService(serviceId).getAuthTokenId();
		if (!StringUtils.isEmpty(authTokenId)) {
			return this.authTokens.get(authTokenId).getCurrentToken();
		}
		return getDefaultAccessToken();
	}

	public void shutdown() throws BaseAppException {
		if (this.threadPool != null) {
			this.threadPool.shutdown();
			try {
				if (!this.threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
					this.threadPool.shutdownNow();
					if (!this.threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
						throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0047"));
					}
				}
			} catch (InterruptedException e) {
				BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0048"));
				Thread.currentThread().interrupt();
				logger.write(LogManager.getServiceLogKey(), ex);
			}
		}
	}

}

class RefreshTokenTask implements Runnable {

	private AuthTokenHandler handler = null;

	RefreshTokenTask(AuthTokenHandler handler) throws BaseAppException {
		this.handler = handler;
		this.handler.getTokenByFirst();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				handler.setCurrentToken(handler.refreshToken());
				Thread.sleep(55000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
