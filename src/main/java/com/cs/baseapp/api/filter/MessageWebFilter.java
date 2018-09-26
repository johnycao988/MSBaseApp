package com.cs.baseapp.api.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.cloud.message.domain.factory.MessageFactory;
import com.cs.log.logs.LogInfoMgr;

@WebFilter("/*")
public class MessageWebFilter implements Filter {

	private static final String APP_CONFIG_FILE = "APP_CONFIG_FILE";

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			MSBaseApplication.doWebFilters(
					MessageFactory.getRequestMessage(convertStreamToString(request.getInputStream())), request,
					response);
		} catch (BaseAppException e) {
			e.printStackTrace();
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		try {
			String appConfigFile = System.getenv().get(APP_CONFIG_FILE);
			if (StringUtils.isEmpty(appConfigFile)) {
				appConfigFile = System.getProperty(APP_CONFIG_FILE);
			}
			if (StringUtils.isEmpty(appConfigFile)) {
				throw new BaseAppException(LogInfoMgr.getErrorInfo(""));
			}
			MSBaseApplication.init(appConfigFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
