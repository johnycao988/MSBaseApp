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
import com.cs.baseapp.utils.MSBaseAppStatus;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.domain.factory.MessageFactory;
import com.cs.commons.jdbc.DSManager;
import com.cs.log.common.logbean.LogInfo;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

@WebFilter("/*")
public class MessageWebFilter implements Filter {

	private static final String CSMSBASEAPP_ROOT_PATH = "CSMSBASEAPP_ROOT_PATH";
	Logger logger = LogManager.getSystemLog();

	public void destroy() {
		try {
			MSBaseApplication.shutdown();
		} catch (BaseAppException e) {
			e.printStackTrace();
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MessageRequest csReqeust = null;
		try {
			if ("application/json".equals(request.getContentType())) {
				csReqeust = MessageFactory.getRequestMessage(convertStreamToString(request.getInputStream()));
				MSBaseApplication.doWebFilters(csReqeust, request, response);
			} else {
				// do other service
			}
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0033"));
			logger.write(LogManager.getServiceLogKey(csReqeust), ex);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		try {
			String rootPath = System.getenv().get(CSMSBASEAPP_ROOT_PATH);
			if (StringUtils.isEmpty(rootPath)) {
				rootPath = System.getProperty(CSMSBASEAPP_ROOT_PATH);
			}
			if (StringUtils.isEmpty(rootPath)) {
				throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0034"));
			}
			initLogback(rootPath);
			initErrorInfo(rootPath);
			initDSInfo(rootPath);
			initBaseApp(rootPath);
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0056"));
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}

	public void initBaseApp(String rootPath) {
		String baseAppConfigFile = rootPath + "/baseConfig/baseAppConfig.yml";
		try {
			MSBaseApplication.init(baseAppConfigFile);
			String strStatus = "Success";
			if (MSBaseApplication.getStatus() != MSBaseAppStatus.RUNNING.getValue()) {
				strStatus = "Fail";
			}
			logger.info(LogManager.getServiceLogKey(),
					"Loading Base Application config File " + strStatus + ". FilePath:" + baseAppConfigFile);
		} catch (BaseAppException e) {
			logger.write(LogManager.getServiceLogKey(), e);
		}
	}

	private void initLogback(String rootPath) {
		String logbackConfigFile = rootPath + "/baseConfig/logback.xml";
		try {
			LogManager.initLogback(logbackConfigFile);
			logger.info(LogManager.getServiceLogKey(),
					"Loading log config file success. FilePath:" + logbackConfigFile);
		} catch (Exception e) {
			LogInfo logInfo = new LogInfo();
			logInfo.setLevel("ERROR");
			logInfo.setMsg("Fail to initialize the logback! FilePath:" + logbackConfigFile);
			logInfo.setCode("ERR_0000");
			BaseAppException ex = new BaseAppException(e, logInfo);
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}

	private void initErrorInfo(String rootPath) {
		String logInfoConfigFile = rootPath + "/baseConfig/baseAppLogInfo.xml";
		try {
			DocumentBuilderFactory d = DocumentBuilderFactory.newInstance();
			Document logConfig = d.newDocumentBuilder().parse(new File(logInfoConfigFile));
			LogInfoMgr.initByDoc("EN", logConfig);
			logger.info(LogManager.getServiceLogKey(),
					"Loading Log information config file success. FilePath:" + logInfoConfigFile);
		} catch (Exception e) {
			LogInfo logInfo = new LogInfo();
			logInfo.setLevel("ERROR");
			logInfo.setMsg("Fail to initialize the LogInfo Message! FilePath:" + logInfoConfigFile);
			logInfo.setCode("ERR_0000");
			BaseAppException ex = new BaseAppException(e, logInfo);
			logger.write(LogManager.getServiceLogKey(), ex);
		}
	}

	private void initDSInfo(String rootPath) {
		String dsConfigFile = rootPath + "/baseConfig/dsConfig.xml";
		try {
			DocumentBuilderFactory doc = DocumentBuilderFactory.newInstance();
			Document dsConfig = doc.newDocumentBuilder().parse(new File(dsConfigFile));
			DSManager.initByDoc(dsConfig);
			logger.info(LogManager.getServiceLogKey(),
					"Loading Data Source congfig file success. FilePath:" + dsConfigFile);
		} catch (Exception e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0055", dsConfigFile));
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
