/**
 * 
 */
package com.cs.unit.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
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
	public void test() throws BaseAppException {
		String appConfigFile = System.getenv().get("CSMSBASEAPP_ROOT_PATH");
		if (StringUtils.isEmpty(appConfigFile)) {
			appConfigFile = System.getProperty("CSMSBASEAPP_ROOT_PATH");
		}
		if (StringUtils.isEmpty(appConfigFile)) {
			throw new BaseAppException(LogInfoMgr.getErrorInfo("ERR_0034"));
		}
		MSBaseApplication.init(appConfigFile + "/baseConfig/baseAppConfig.yml");
	}

}
