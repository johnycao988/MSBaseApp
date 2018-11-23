/**
 * 
 */
package com.cs.unit.test;

import java.util.Properties;

import com.cs.baseapp.api.messagebroker.MessageSender;
import com.cs.baseapp.api.messagebroker.TranslationMessage;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.cloud.message.domain.factory.MessageFactory;

/**
 * @author Donald.Wang
 *
 */
public class TestSender extends MessageSender {

	public TestSender(String id, Properties prop) {
		super(id, prop);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize() throws BaseAppException {
		System.out.println(Thread.currentThread().getName() + ": TestSender init!");
	}

	@Override
	public void sendAsyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		System.out.println("Send a Async Message!");

	}

	@Override
	public void close() throws BaseAppException {
		System.out.println("The connection is close!");

	}

	@Override
	public MessageResponse sendSyncMessage(TranslationMessage requestMsg) throws BaseAppException {
		System.out.println(Thread.currentThread().getName() + ": Send a Sync Message!");
		MessageResponse testRes = null;
		try {
			testRes = MessageFactory.getResopnseMessage(
					"{\"head\":{\"base\":{\"correlationId\":\"fde703ae-5270-0ea5-bdfc-4ba13231cd67\",\"expireTime\":2075310394,\"sequence\":\"1/1\",\"type\":1,\"id\":\"f83bf7ef-c526-489f-a770-e921f1bf4cf5\",\"timeStamp\":1539673602414}},\"body\":[{\"applicationId\":\"StandingData\",\"id\":\"eloan-test\",\"status\":{\"success\":true,\"errCode\":\"200\"},\"resData\":[{\"C_CNTY_NAME\":\"ASASSA\",\"C_CNTY_CODE\":\"AA\",\"C_TRX_STATUS\":\"M\",\"C_AREA_CODE\":\"AA\",\"C_CNTY_CODE1\":\"AAA\",\"C_CNTY_CODE2\":\"232\"}]}]}");
		} catch (MessageException e) {
			e.printStackTrace();
		}
		return testRes;
	}

	@Override
	public void sendAsyncRespMessage(MessageResponse resp) throws BaseAppException {
		// TODO Auto-generated method stub
		
	}

}
