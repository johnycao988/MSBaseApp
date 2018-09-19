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

	@SuppressWarnings("unchecked")
	public void load(InputStream is) throws BaseAppException {
		try {
			Yaml yamlConfig = new Yaml();
			Map<String, Object> m = yamlConfig.load(is);
			Map<String, Object> dtl = (Map<String, Object>) m.entrySet().iterator().next().getValue();
			this.baseConfig = (Map<String, String>) dtl.get("base");
			this.envConfig = (Map<String, String>) dtl.get("env");
			this.filterConfig = (List<Map<String, Object>>) dtl.get("webMessageFilter");
			Map<String, Object> mbConfig = (Map<String, Object>) dtl.get("messageBroker");
			this.mbSendersCondig = (List<Map<String, Object>>) mbConfig.get("sender");
			this.mbReceiversConfig = (List<Map<String, Object>>) mbConfig.get("receiver");
			this.mbListenersConfig = (List<Map<String, Object>>) mbConfig.get("listener");
			Map<String, Object> serviceConfig = (Map<String, Object>) mbConfig.get("services");
			this.mbLoaclServicesConfig = (List<Map<String, Object>>) serviceConfig.get("local");
			this.mbRemoteServicesConfig = (List<Map<String, Object>>) serviceConfig.get("remote");
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0003"));
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

}
