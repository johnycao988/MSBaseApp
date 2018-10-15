/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Donald.Wang
 *
 */
public abstract class BaseMessageFilter implements MessageFilter {

	protected String id;

	protected String urlPattern;

	protected Properties prop;

	public BaseMessageFilter(String id, String urlPattern, Properties prop) {
		this.id = id;
		this.urlPattern = urlPattern;
		this.prop = prop;
	}

	public BaseMessageFilter(String id, Properties prop) {
		this.id = id;
		this.prop = prop;
	}

	public String getId() {
		return this.id;
	}

	public String getUrlPattern() {
		return this.urlPattern;
	}

	public String getProperty(String key) {
		if (this.prop != null && !StringUtils.isEmpty(key)) {
			return this.prop.getProperty(key);
		}
		return null;
	}

}
