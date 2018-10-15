/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.baseapp.utils.ConfigConstant;
import com.cs.baseapp.utils.PropertiesUtils;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;
import com.cs.log.logs.bean.ServiceLogKey;

/**
 * @author Donald.Wang
 *
 */
public class FilterFactory {

	private static ServiceLogKey logKey = LogManager.getServiceLogKey();

	private static final Logger logger = LogManager.getSystemLog();

	private FilterFactory() {

	}

	public static List<BaseMessageFilter> buildWebFilters(List<Map<String, Object>> filtersConfig) {
		List<BaseMessageFilter> filters = new ArrayList<>();
		if (filtersConfig == null || filtersConfig.isEmpty()) {
			return filters;
		}
		logger.info(logKey, "Start to build WebFilters.");
		for (Map<String, Object> singleConfig : filtersConfig) {
			BaseMessageFilter filter = buildWebFilter(singleConfig);
			if (filter == null) {
				continue;
			}
			filters.add(filter);
		}
		return filters;
	}

	@SuppressWarnings("unchecked")
	private static BaseMessageFilter buildWebFilter(Map<String, Object> filterConfig) {
		Object instance = null;
		String filterId = (String) filterConfig.get(ConfigConstant.ID.getValue());
		String implClass = (String) filterConfig.get(ConfigConstant.IMPL_CLASS.getValue());
		try {
			instance = Class.forName(implClass).getConstructor(String.class, String.class, Properties.class)
					.newInstance(filterId, filterConfig.get(ConfigConstant.URLPATTERN.getValue()),
							PropertiesUtils.convertMapToProperties(
									(Map<String, String>) filterConfig.get(ConfigConstant.PARAMETERS.getValue())));
			logger.info(logKey, "Build web filter success. FilterId:" + filterId + " ImplementClass:" + implClass);
		} catch (Exception e) {
			BaseAppException baseAppException = new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0007", filterId, implClass));
			logger.write(logKey, baseAppException);
		}
		return (BaseMessageFilter) instance;
	}

	public static List<BaseMessageFilter> buildListenerFilters(List<Map<String, Object>> configs)
			throws BaseAppException {
		List<BaseMessageFilter> filters = new ArrayList<>();
		if (configs == null || configs.isEmpty()) {
			return filters;
		}
		logger.info(logKey, "Start to build Listener Filters.");
		for (Map<String, Object> config : configs) {
			filters.add(buildListenerFilter(config));
		}
		return filters;
	}

	@SuppressWarnings("unchecked")
	public static BaseMessageFilter buildListenerFilter(Map<String, Object> listenerConfig) throws BaseAppException {
		Object instance = null;
		try {
			String filterId = (String) listenerConfig.get(ConfigConstant.ID.getValue());
			String filterClass = (String) listenerConfig.get(ConfigConstant.MESSAGE_FILTER.getValue());
			instance = Class.forName(filterClass).getConstructor(String.class, Properties.class)
					.newInstance(filterId, PropertiesUtils.convertMapToProperties(
							(Map<String, String>) listenerConfig.get(ConfigConstant.PARAMETERS.getValue())));
			logger.info(logKey, "Build listener filter succsess. Id: " + filterId + " ImplementClass:" + filterClass);
		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0008", listenerConfig.get(ConfigConstant.MESSAGE_FILTER.getValue())));
		}
		return (BaseMessageFilter) instance;
	}

}
