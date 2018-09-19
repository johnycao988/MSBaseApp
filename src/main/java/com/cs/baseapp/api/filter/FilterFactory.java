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

	private static final Logger logger = Logger.getLogger("SYSTEM");

	private static final String IMPL_CLASS = "implementClass";

	private static final String MSG_FILTER = "messageFilter";

	private FilterFactory() {

	}

	public static List<MessageFilter> buildWebFilters(List<Map<String, Object>> filtersConfig) throws BaseAppException {
		List<MessageFilter> filters = new ArrayList<>();
		logger.info(logKey, "Start to build WebFilters.");
		for (Map<String, Object> singleConfig : filtersConfig) {
			filters.add(buildWebFilter(singleConfig));
		}
		return filters;
	}

	@SuppressWarnings("unchecked")
	private static BaseMessageFilter buildWebFilter(Map<String, Object> filterConfig) throws BaseAppException {
		Object instance = null;
		try {
			instance = Class.forName((String) filterConfig.get(IMPL_CLASS))
					.getConstructor(String.class, String.class, Properties.class)
					.newInstance(filterConfig.get("id"), filterConfig.get("urlPattern"), PropertiesUtils
							.convertMapToProperties((Map<String, String>) filterConfig.get("parameters")));
			logger.info(logKey, "Build web filter success. FilterId:" + filterConfig.get("id") + " ImplementClass"
					+ filterConfig.get(IMPL_CLASS));
		} catch (Exception e) {
			throw new BaseAppException(e,
					LogInfoMgr.getErrorInfo("ERR_0006", filterConfig.get("id"), filterConfig.get(IMPL_CLASS)));
		}
		return (BaseMessageFilter) instance;
	}

	@SuppressWarnings("unchecked")
	public static BaseMessageFilter buildListenerFilter(Map<String, Object> listenerConfig) throws BaseAppException {
		Object instance = null;
		try {
			instance = Class.forName((String) listenerConfig.get(MSG_FILTER)).getConstructor(Properties.class)
					.newInstance(PropertiesUtils
							.convertMapToProperties((Map<String, String>) listenerConfig.get("parameters")));
			logger.info(logKey, "Build listener filter succsess. ListenerId" + listenerConfig.get("id")
					+ "ImplementClass:" + listenerConfig.get(MSG_FILTER));
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0007", listenerConfig.get(MSG_FILTER)));
		}
		return (BaseMessageFilter) instance;
	}

}
