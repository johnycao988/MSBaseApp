/**
 * 
 */
package com.cs.baseapp.api.filter;

import java.util.Properties;

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

	public BaseMessageFilter(Properties prop) {
		this.prop = prop;
	}

}
