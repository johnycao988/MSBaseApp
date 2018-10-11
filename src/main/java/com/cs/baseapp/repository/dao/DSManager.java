/**
 * 
 */
package com.cs.baseapp.repository.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.commons.jdbc.IJdbcExec;
import com.cs.commons.jdbc.JdbcManager;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class DSManager {

	private static Map<String, String> schemaMap = new HashMap<>();

	private static Map<String, String> dsMap = new HashMap<>();

	private static Logger logger = LogManager.getSystemLog();

	private DSManager() {

	}

	public static void initDS(Map<String, String> schemas, Map<String, String> dataSources) {
		schemaMap = schemas;
		dsMap = dataSources;
		Set<Entry<String, String>> entrys = dataSources.entrySet();
		for (Entry<String, String> entry : entrys) {
			try {
				JdbcManager.addDSInfo(entry.getValue());
			} catch (Exception e) {
				logger.write(LogManager.getServiceLogKey(),
						new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0037", entry.getValue())));
			}
		}
	}

	public static String getDs(String unitCode) {
		if (dsMap.containsKey(unitCode)) {
			return dsMap.get(unitCode);
		}
		return getDefaultDs();
	}

	public static String getDefaultDs() {
		return dsMap.get("default");
	}

	public static IJdbcExec getJdbcExec(String unitCode) {
		return JdbcManager.getJdbcExec(getDs(unitCode));
	}

	public static IJdbcExec getDefaultJdbcExec() {
		return JdbcManager.getJdbcExec(getDefaultDs());
	}

	public static String getSchema(String unitCode) {
		if (schemaMap.containsKey(unitCode)) {
			schemaMap.get(unitCode);
		}
		return getDefaultSchema();
	}

	public static String getDefaultSchema() {
		return schemaMap.get("default");
	}

}
