/**
 * 
 */
package com.cs.baseapp.utils;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author Donald.Wang
 *
 */
public class PropertiesUtils {

	private PropertiesUtils() {

	}

	public static Properties convertMapToProperties(Map<String, String> map) {
		Properties p = new Properties();
		if (map == null) {
			return null;
		}
		Set<Entry<String, String>> set = map.entrySet();
		for (Entry<String, String> e : set) {
			p.setProperty(e.getKey(), String.valueOf(e.getValue()));
		}
		return p;
	}
}
