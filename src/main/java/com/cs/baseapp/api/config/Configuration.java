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
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class Configuration {

	private Map<String, String> baseConfig = new HashMap<>();

	private Map<String, String> envConfig = new HashMap<>();

	private List<Map<String, Object>> filterConfig = new ArrayList<>();

	private List<Map<String, Object>> mbSendersCondig = new ArrayList<>();

	private List<Map<String, Object>> mbReceiversConfig = new ArrayList<>();

	private List<Map<String, Object>> mbListenersConfig = new ArrayList<>();

	private List<Map<String, Object>> mbLoaclServicesConfig = new ArrayList<>();

	private List<Map<String, Object>> mbRemoteServicesConfig = new ArrayList<>();

	private Map<String, String> repositoryConfig = new HashMap<>();

	@SuppressWarnings("unchecked")
	public void load(InputStream is) throws BaseAppException {
		try {
			Yaml yamlConfig = new Yaml();
			Map<String, Object> m = yamlConfig.load(is);
			Map<String, Object> dtl = (Map<String, Object>) m.entrySet().iterator().next().getValue();
			this.baseConfig = (Map<String, String>) dtl.get(ConfigConstant.BASE.getValue());
			this.envConfig = (Map<String, String>) dtl.get(ConfigConstant.EVN.getValue());
			this.filterConfig = (List<Map<String, Object>>) dtl.get(ConfigConstant.WEB_MESSAGE_FILTER.getValue());
			Map<String, Object> mbConfig = (Map<String, Object>) dtl.get(ConfigConstant.MESSAGE_BROKER.getValue());
			this.mbSendersCondig = (List<Map<String, Object>>) mbConfig.get(ConfigConstant.SENDER.getValue());
			this.mbReceiversConfig = (List<Map<String, Object>>) mbConfig.get(ConfigConstant.RECRIVER.getValue());
			this.mbListenersConfig = (List<Map<String, Object>>) mbConfig.get(ConfigConstant.LISTENER.getValue());
			Map<String, Object> serviceConfig = (Map<String, Object>) mbConfig.get(ConfigConstant.SERVICES.getValue());
			this.mbLoaclServicesConfig = (List<Map<String, Object>>) serviceConfig
					.get(ConfigConstant.LOACL_SERVICE.getValue());
			this.mbRemoteServicesConfig = (List<Map<String, Object>>) serviceConfig
					.get(ConfigConstant.REMOTE_SERVICE.getValue());
			Map<String, Object> repositoryConfigs = (Map<String, Object>) mbConfig
					.get(ConfigConstant.REPOSITORY.getValue());
			this.repositoryConfig = (Map<String, String>) repositoryConfigs.get(ConfigConstant.PARAMETERS.getValue());
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0004"));
		}
	}

	public Map<String, String> getBaseConfig() {
		return this.baseConfig;
	}

	public Map<String, String> getEnvConfig() {
		return this.envConfig;
	}

	public List<Map<String, Object>> getFilterConfig() {
		return this.filterConfig;
	}

	public List<Map<String, Object>> getMbSendersConfig() {
		return this.mbSendersCondig;
	}

	public List<Map<String, Object>> getMbReceiversConfig() {
		return this.mbReceiversConfig;
	}

	public List<Map<String, Object>> getMbListenersConfig() {
		return this.mbListenersConfig;
	}

	public List<Map<String, Object>> getMbLocalServicesConfig() {
		return this.mbLoaclServicesConfig;
	}

	public List<Map<String, Object>> getMbRemoteServicesConfig() {
		return this.mbRemoteServicesConfig;
	}

	public Map<String, String> getRepositoryConfig() {
		return this.repositoryConfig;
	}

}
