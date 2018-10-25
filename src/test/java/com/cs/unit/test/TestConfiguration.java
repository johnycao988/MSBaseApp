/**
 * 
 */
package com.cs.unit.test;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.log.common.logbean.LogInfo;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class TestConfiguration {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("CSMSBASEAPP_ROOT_PATH", "D:\\BaseApp");
	}

	@Test
	public void test() throws Exception {
		String appConfigFile = System.getenv().get("CSMSBASEAPP_ROOT_PATH");
		if (StringUtils.isEmpty(appConfigFile)) {
			appConfigFile = System.getProperty("CSMSBASEAPP_ROOT_PATH");
		}
		if (StringUtils.isEmpty(appConfigFile)) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0034"));
		}
		initLogback(appConfigFile);
		DocumentBuilderFactory d = DocumentBuilderFactory.newInstance();
		Document logConfig = d.newDocumentBuilder().parse(new File(appConfigFile + "/baseConfig/baseAppLogInfo.xml"));
		LogInfoMgr.initByDoc("EN", logConfig);
		MSBaseApplication.init(appConfigFile + "/baseConfig/baseAppConfig.yml");
	}

	private void initLogback(String rootPath) {
		String logbackConfigFile = rootPath + "/baseConfig/logback.xml";
		try {
			LogManager.initLogback(logbackConfigFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
