/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.api.auth.AuthRule;

/**
 * @author Donald.Wang
 *
 */
public abstract class BaseMessageFilter implements MessageFilter {

	protected String id;

	protected String urlPattern;

	protected Properties prop;

	protected String authRuleId;

	public BaseMessageFilter(String id, String urlPattern, Properties prop, String authRuleId) {
		this.id = id;
		this.urlPattern = urlPattern;
		this.prop = prop;
		this.authRuleId = authRuleId;
	}

	public BaseMessageFilter(String id, Properties prop,String authRuleId) {
		this.id = id;
		this.prop = prop;
		this.authRuleId = authRuleId;
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

	public AuthRule getAuthRule() {
		return MSBaseApplication.getAuthManager().getAuthRule(this.authRuleId);
	}

}
