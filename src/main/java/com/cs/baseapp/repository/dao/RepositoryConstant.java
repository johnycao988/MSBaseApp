/**
 * 
 */
package com.cs.baseapp.repository.dao;

/**
 * @author Donald.Wang
 *
 */
public enum RepositoryConstant {

	TB_BASE_APP_MSG("BASE_APP_MSG"), COL_MESSAGE_ID("MESSAGE_ID"), COL_SERVICE_ID("SERVICE_ID"), COL_APP_ID("APP_ID"),
	COL_CORRELATION_ID("CORRELATION_ID"), COL_UNIT_CODE("UNIT_CODE"), COL_FUNC_ID("FUNC_ID"), COL_TRX_NO("TRX_NO"),
	COL_REF_NO("REF_NO"), COL_USER_ID("USER_ID"), COL_MESSAGE_CONTENT("MESSAGE_CONTENT"),
	COL_FROM_SERVICE("FROM_SERVICE"), COL_TO_SERVICE("TO_SERVICE"), CREATE_TIME("CREATE_TIME");

	private String value;

	private RepositoryConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
