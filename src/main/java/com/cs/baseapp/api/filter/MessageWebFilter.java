package com.cs.baseapp.api.filter;

import java.io.BufferedReader;
import java.io.File;
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
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.factory.MessageFactory;
import com.cs.commons.jdbc.DSManager;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

@WebFilter("/*")
public class MessageWebFilter implements Filter {

	private static final String CSMSBASEAPP_ROOT_PATH = "CSMSBASEAPP_ROOT_PATH";
	Logger logger = LogManager.getSystemLog();

	public void destroy() {
		// Do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MessageRequest csReqeust = null;
		try {
			csReqeust = MessageFactory.getRequestMessage(convertStreamToString(request.getInputStream()));
			MSBaseApplication.doWebFilters(csReqeust, request, response);
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0033"));
			logger.write(LogManager.getServiceLogKey(csReqeust), ex);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		initBaseApp();
	}

	public void initBaseApp() {
		try {
			String appConfigFile = System.getenv().get(CSMSBASEAPP_ROOT_PATH);
			if (StringUtils.isEmpty(appConfigFile)) {
				appConfigFile = System.getProperty(CSMSBASEAPP_ROOT_PATH);
			}
			if (StringUtils.isEmpty(appConfigFile)) {
				throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0034"));
			}
			LogManager.initLogback(appConfigFile + "/baseConfig/logback.xml");
			logger.info(LogManager.getServiceLogKey(),
					"Loading log config file success. FilePath:" + appConfigFile + "/baseConfig/logback.xml");
			DocumentBuilderFactory d = DocumentBuilderFactory.newInstance();
			Document logConfig = d.newDocumentBuilder()
					.parse(new File(appConfigFile + "/baseConfig/baseAppLogInfo.xml"));
			LogInfoMgr.initByDoc("EN", logConfig);
			logger.info(LogManager.getServiceLogKey(), "Loading Log information config file success. FilePath:"
					+ appConfigFile + "/baseConfig/baseAppLogInfo.xml");
			DocumentBuilderFactory doc = DocumentBuilderFactory.newInstance();
			Document dsConfig = doc.newDocumentBuilder().parse(new File(appConfigFile + "/baseConfig/dsConfig.xml"));
			DSManager.initByDoc(dsConfig);
			logger.info(LogManager.getServiceLogKey(),
					"Loading Data Source congfig file success. FilePath:" + appConfigFile + "/baseConfig/dsConfig.xml");
			MSBaseApplication.init(appConfigFile + "/baseConfig/baseAppConfig.yml");
			logger.info(LogManager.getServiceLogKey(), "Loading Base Application config File success. FilePath:"
					+ appConfigFile + "/baseConfig/baseAppConfig.yml");
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0035"));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}

	private String convertStreamToString(InputStream is) throws BaseAppException {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0036"));
		}
		return sb.toString();
	}

}
