/**
 * 
 */
package com.cs.baseapp.api.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class Configuration {

	private Map<String, String> baseConfig = new HashMap<>();

	private Map<String, String> envConfig = new HashMap<>();

	private List<Map<String, Object>> filterConfig = new ArrayList<>();

	private Map<String, Object> authRulesConfig = new HashMap<>();

	private List<Map<String, Object>> mbSendersCondig = new ArrayList<>();

	private List<Map<String, Object>> mbReceiversConfig = new ArrayList<>();

	private List<Map<String, Object>> mbListenersConfig = new ArrayList<>();

	private List<Map<String, Object>> mbLoaclServicesConfig = new ArrayList<>();

	private List<Map<String, Object>> mbRemoteServicesConfig = new ArrayList<>();

	private Map<String, String> repositoryConfig = new HashMap<>();

	private Logger logger = LogManager.getSystemLog();

	private String processKey = "";

	@SuppressWarnings("unchecked")
	public void load(InputStream is) {
		try {
			Yaml yamlConfig = new Yaml();
			Map<String, Object> m = yamlConfig.load(is);
			Map<String, Object> dtl = (Map<String, Object>) m.entrySet().iterator().next().getValue();
			this.processKey = ConfigConstant.BASE.getValue();
			this.baseConfig = (Map<String, String>) dtl.get(ConfigConstant.BASE.getValue());
			this.processKey = ConfigConstant.ENV.getValue();
			this.envConfig = (Map<String, String>) dtl.get(ConfigConstant.ENV.getValue());
			this.processKey = ConfigConstant.AUTH_RULES.getValue();
			this.authRulesConfig = (Map<String, Object>) dtl.get(ConfigConstant.AUTH_RULES.getValue());
			this.processKey = ConfigConstant.WEB_MESSAGE_FILTER.getValue();
			this.filterConfig = (List<Map<String, Object>>) dtl.get(ConfigConstant.WEB_MESSAGE_FILTER.getValue());
			this.processKey = ConfigConstant.MESSAGE_BROKER.getValue();
			Map<String, Object> mbConfig = (Map<String, Object>) dtl.get(ConfigConstant.MESSAGE_BROKER.getValue());
			this.processKey = ConfigConstant.SENDER.getValue();
			this.mbSendersCondig = (List<Map<String, Object>>) mbConfig.get(ConfigConstant.SENDER.getValue());
			this.processKey = ConfigConstant.RECRIVER.getValue();
			this.mbReceiversConfig = (List<Map<String, Object>>) mbConfig.get(ConfigConstant.RECRIVER.getValue());
			this.processKey = ConfigConstant.LISTENER.getValue();
			this.mbListenersConfig = (List<Map<String, Object>>) mbConfig.get(ConfigConstant.LISTENER.getValue());
			this.processKey = ConfigConstant.SERVICES.getValue();
			Map<String, Object> serviceConfig = (Map<String, Object>) mbConfig.get(ConfigConstant.SERVICES.getValue());
			this.processKey = ConfigConstant.LOACL_SERVICE.getValue();
			this.mbLoaclServicesConfig = (List<Map<String, Object>>) serviceConfig
					.get(ConfigConstant.LOACL_SERVICE.getValue());
			this.processKey = ConfigConstant.REMOTE_SERVICE.getValue();
			this.mbRemoteServicesConfig = (List<Map<String, Object>>) serviceConfig
					.get(ConfigConstant.REMOTE_SERVICE.getValue());
			this.processKey = ConfigConstant.REPOSITORY.getValue();
			Map<String, Object> repositoryConfigs = (Map<String, Object>) mbConfig
					.get(ConfigConstant.REPOSITORY.getValue());
			if (repositoryConfigs != null) {
				this.repositoryConfig = (Map<String, String>) repositoryConfigs
						.get(ConfigConstant.PARAMETERS.getValue());
			}
		} catch (Exception e) {
			logger.write(LogManager.getServiceLogKey(),
					new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0004", this.processKey)));
		}
	}

	public Map<String, String> getBaseConfig() {
		return this.baseConfig == null ? new HashMap<>() : this.baseConfig;
	}

	public Map<String, String> getEnvConfig() {
		return this.envConfig == null ? new HashMap<>() : this.envConfig;
	}

	public List<Map<String, Object>> getFilterConfig() {
		return this.filterConfig == null ? new ArrayList<>() : this.filterConfig;
	}

	public List<Map<String, Object>> getMbSendersConfig() {
		return this.mbSendersCondig == null ? new ArrayList<>() : this.mbSendersCondig;
	}

	public List<Map<String, Object>> getMbReceiversConfig() {
		return this.mbReceiversConfig == null ? new ArrayList<>() : this.mbReceiversConfig;
	}

	public List<Map<String, Object>> getMbListenersConfig() {
		return this.mbListenersConfig == null ? new ArrayList<>() : this.mbListenersConfig;
	}

	public List<Map<String, Object>> getMbLocalServicesConfig() {
		return this.mbLoaclServicesConfig == null ? new ArrayList<>() : this.mbLoaclServicesConfig;
	}

	public List<Map<String, Object>> getMbRemoteServicesConfig() {
		return this.mbRemoteServicesConfig == null ? new ArrayList<>() : this.mbRemoteServicesConfig;
	}

	public Map<String, String> getRepositoryConfig() {
		return this.repositoryConfig == null ? new HashMap<>() : this.repositoryConfig;
	}

	public Map<String, Object> getAuthRulesConfig() {
		return this.authRulesConfig == null ? new HashMap<>() : this.authRulesConfig;
	}

}
