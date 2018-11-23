/**
 * 
 */
package com.cs.baseapp.utils;

/**
 * @author Donald.Wang
 *
 */
public enum ConfigConstant {

	APP("app"), BASE("base"), BASE_NAME("name"), BASE_VERSION("version"), BASE_APP_ID("applicationId"),
	BASE_SECRET("secret"), ENV("env"), WEB_MESSAGE_FILTER("webMessageFilter"), URLPATTERN("urlPattern"),
	MESSAGE_BROKER("messageBroker"), SENDER("sender"), POOL_SZIE("poolSize"), REST_METHOD("METHOD"),
	REST_PARA_TYPE("PARA_TYPE"), RECRIVER("receiver"), PARAMETERS("parameters"), ID("id"), IMPL_CLASS("implementClass"),
	JMS_USER_NAME("USER_NAME"), JMS_USER_PWD("USER_PWD"), URI("URI"), JMS_QUEUE_NAME("QUEUE_NAME"),
	LISTENER("listener"), MESSAGE_FILTER("messageFilter"), TRANS_CLASS("tranformClass"),
	MAX_PROCESS_THREADS("maxProcessThreads"), SERVICES("services"), LOACL_SERVICE("local"), REMOTE_SERVICE("remote"),
	SENDER_ID("senderId"), RECEIVER_ID("receiverId"), TIMEOUT("timeout"),
	JMS_CONNECTION_FACTORY_JNDI("CONN_FACTORY_JNDI"), JMS_QUEUE_JNDI("QUEUE_JNDI"), REPOSITORY("repository"),
	DATA_SOURCE("dataSource"), SCHEMAS("schemas"), STORE_MSG("storeMsg"), LOG_NAME("logName"),
	CONNECTIONS("connections"), AUTH_RULES("authRules"), ROLES("roles"), IS_AUTH("isAuth"), ROLES_FUNC("func"),
	ROLES_SERVICE("service"), ROLES_ORG_UNIT("orgUnit"), ROLES_USER("user"), CLIENT_ID("clientId"),
	AUTH_TOKEN("authToken"), RULES("rules"), AUTH_TOKEN_ID("authTokenId"), CLIENTS("clients"),
	AUTH_RULE_ID("authRuleId"), USERINFO_ENDPOINT("userinfo_endpoint"), IS_DEFAULT("isDefault"),
	ASYNC_RESPONSE_SENDER_ID("asyncRespSenderId");

	private String value;

	private ConfigConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
