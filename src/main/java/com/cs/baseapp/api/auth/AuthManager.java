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
		logger.info(LogManager.getServiceLogKey(), "Start build AuthTokenHandler.");
		buildAuthTokenHandlers((List<Map<String, Object>>) configs.get(ConfigConstant.AUTH_TOKEN.getValue()));
		logger.info(LogManager.getServiceLogKey(), "Build AuthTokenHandler Finish.");
		logger.info(LogManager.getServiceLogKey(), "Start build AuthRules.");
		buildAuthRules((List<Map<String, Object>>) configs.get(ConfigConstant.RULES.getValue()));
		logger.info(LogManager.getServiceLogKey(), "Build AuthRules finish.");
	}

	public void start() {
		this.threadPool = Executors.newFixedThreadPool(this.authRules.size());
		Set<Entry<String, AuthTokenHandler>> set = this.authTokens.entrySet();
		for (Entry<String, AuthTokenHandler> e : set) {
			try {
				this.threadPool.execute(new RefreshTokenTask(e.getValue()));
			} catch (BaseAppException e1) {
				logger.write(LogManager.getServiceLogKey(),
						new BaseAppException(e1, LogInfoMgr.getErrorInfo("ERR_0057", e.getValue().getId())));
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
				logger.info(LogManager.getServiceLogKey(), "Build AuthRole Success! AuthRoleId: " + id);
			} catch (Exception e) {
				logger.write(LogManager.getServiceLogKey(),
						new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0058", id)));
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
				logger.info(LogManager.getServiceLogKey(),
						"Build AuthTokenHandler Success! AuthTokenId: " + authTokenId);
				if (headler.isDefault()) {
					this.defaultAuthTokenId = headler.getId();
					logger.info(LogManager.getServiceLogKey(), "Set the " + authTokenId + " as the Default AuthToken!");
				}
			} catch (Exception e) {
				logger.write(LogManager.getServiceLogKey(),
						new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0059", authTokenId, implClass)));
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
		logger.info(LogManager.getServiceLogKey(), "start to stop AuthManager!");
		if (this.threadPool != null) {
			this.threadPool.shutdown();
			try {
				if (!this.threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
					this.threadPool.shutdownNow();
					if (!this.threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
						throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0060"));
					}
				}
				logger.info(LogManager.getServiceLogKey(), "stop AuthToken Thread Pool Finish!");
				this.authRules.clear();
				this.authTokens.clear();
			} catch (InterruptedException e) {
				BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0061"));
				Thread.currentThread().interrupt();
				logger.write(LogManager.getServiceLogKey(), ex);
			}
		}
		logger.info(LogManager.getServiceLogKey(), "Stop AuthManager Finish!");
	}

}

class RefreshTokenTask implements Runnable {

	private AuthTokenHandler handler = null;

	private Logger logger = LogManager.getSystemLog();

	RefreshTokenTask(AuthTokenHandler handler) throws BaseAppException {
		this.handler = handler;
		this.handler.getTokenByFirst();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				handler.setCurrentToken(handler.refreshToken());
				Thread.sleep((long) handler.getExpireTime() * 1000 - 60000);
			}
		} catch (InterruptedException e1) {
			logger.info(LogManager.getServiceLogKey(), "RefreshTokenTask stop finish! AuthTokenId:" + handler.getId());
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			logger.write(LogManager.getServiceLogKey(),
					new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0062", handler.getId())));
		}
	}

}
