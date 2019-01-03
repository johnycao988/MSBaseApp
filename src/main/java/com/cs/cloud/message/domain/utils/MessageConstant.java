package com.cs.cloud.message.domain.utils;

public class MessageConstant {

	private MessageConstant() {

	}

	public static final String MESSAGE_HEAD = "head";
	public static final String MESSAGE_HEAD_BASE = "base";
	public static final String MESSAGE_HEAD_BASE_ID = "id";
	public static final String MESSAGE_HEAD_BASE_TIMESTAMP = "timeStamp";
	public static final String MESSAGE_HEAD_BASE_EXPIRE_TIME = "expireTime";
	public static final String MESSAGE_HEAD_BASE_CORRELATION_ID = "correlationId";
	public static final String MESSAGE_HEAD_BASE_SEQUENCE = "sequence";
	public static final String MESSAGE_HEAD_BASE_TYPE = "type";
	public static final int MESSAGE_HEAD_BASE_TYPE_REQUEST = 0;
	public static final int MESSAGE_HEAD_BASE_TYPE_RESPONSE = 1;
	public static final String MESSAGE_HEAD_CONSUMER = "consumer";
	public static final String MESSAGE_HEAD_CONSUMER_CLIENT_ID = "clientId";
	public static final String MESSAGE_HEAD_CONSUMER_ID = "id";
	public static final String MESSAGE_HEAD_CONSUMER_SECRET = "secret";
	public static final String MESSAGE_HEAD_CONSUMER_TOKEN = "token";
	public static final String MESSAGE_HEAD_CONSUMER_USER_NAME = "userName";
	public static final String MESSAGE_HEAD_SERVICES = "services";
	public static final String MESSAGE_HEAD_SERVICE_APPLICATION_ID = "applicationId";
	public static final String MESSAGE_HEAD_SERVICE_ID = "id";
	public static final String MESSAGE_HEAD_SERVICE_SYNC = "sync";
	public static final String MESSAGE_HEAD_SERVICE_PROPERTIES = "properties";
	public static final String MESSAGE_HEAD_TRANSACTION = "trx";
	public static final String MESSAGE_HEAD_TRANSACTION_TRX_NO = "trxNo";
	public static final String MESSAGE_HEAD_TRANSACTION_UNIC_CODE = "unitCode";
	public static final String MESSAGE_HEAD_TRANSACTION_FUNCTION_ID = "funcId";
	public static final String MESSAGE_HEAD_TRANSACTION_REFERENCE_NO = "refNo";
	public static final String MESSAGE_BODY = "body";
	public static final String MESSAGE_BODY_SERVICE_ID = "id";
	public static final String MESSAGE_BODY_SERVICE_REQUEST_DATA = "reqData";
	public static final String MESSAGE_BODY_SERVICE_APPLICATION_ID = MESSAGE_HEAD_SERVICE_APPLICATION_ID;
	public static final String MESSAGE_BODY_SERVICE_SECRET = MESSAGE_HEAD_CONSUMER_SECRET;
	public static final String MESSAGE_BODY_SERVICE_TOKEN = MESSAGE_HEAD_CONSUMER_TOKEN;
	public static final String MESSAGE_BODY_SERVICE_USER_NAME = MESSAGE_HEAD_CONSUMER_USER_NAME;
	public static final String MESSAGE_BODY_SERVICE_STATUS = "status";
	public static final String MESSAGE_BODY_SERVICE_STATUS_SUCCESS = "success";
	public static final String MESSAGE_BODY_SERVICE_ERROR_CODE = "errCode";
	public static final String MESSAGE_BODY_SERVICE_ERROR_MESSAGE = "errMsg";
	public static final String MESSAGE_BODY_SERVICE_RESPONSE_DATA = "resData";

}
