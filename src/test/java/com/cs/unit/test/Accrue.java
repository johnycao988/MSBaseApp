/**
 * 
 */
package com.cs.unit.test;

import java.util.Properties;

import com.cs.baseapp.api.messagebroker.BusinessService;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.cloud.message.domain.errorhandling.MessageException;
import com.cs.cloud.message.domain.factory.MessageFactory;

/**
 * @author Donald.Wang
 *
 */
public class Accrue extends BusinessService {

	public Accrue(MessageRequest req, Properties prop) {
		super(req, prop);
	}

	@Override
	public MessageResponse process() throws BaseAppException, MessageException {
		System.out.println("RequestMessage:" + super.getReq().getJsonString());
		MessageResponse accRes = MessageFactory.getResopnseMessage(
				"{\"head\":{\"base\":{\"correlationId\":\"fde703ae-5270-0ea5-bdfc-4ba13231cd67\",\"expireTime\":2075310394,\"sequence\":\"1/1\",\"type\":1,\"id\":\"f83bf7ef-c526-489f-a770-e921f1bf4cf5\",\"timeStamp\":1539673602414}},\"body\":[{\"applicationId\":\"StandingData\",\"id\":\"eloan-accrue\",\"status\":{\"success\":true,\"errCode\":\"200\"},\"resData\":[{\"C_CNTY_NAME\":\"ASASSA\",\"C_CNTY_CODE\":\"AA\",\"C_TRX_STATUS\":\"M\",\"C_AREA_CODE\":\"AA\",\"C_CNTY_CODE1\":\"AAA\",\"C_CNTY_CODE2\":\"232\"}]}]}");
		MessageResponse testRes = MessageFactory.getResopnseMessage(
				"{\"head\":{\"base\":{\"correlationId\":\"fde703ae-5270-0ea5-bdfc-4ba13231cd67\",\"expireTime\":2075310394,\"sequence\":\"1/1\",\"type\":1,\"id\":\"f83bf7ef-c526-489f-a770-e921f1bf4cf5\",\"timeStamp\":1539673602414}},\"body\":[{\"applicationId\":\"StandingData\",\"id\":\"eloan-test\",\"status\":{\"success\":true,\"errCode\":\"200\"},\"resData\":[{\"C_CNTY_NAME\":\"ASASSA\",\"C_CNTY_CODE\":\"AA\",\"C_TRX_STATUS\":\"M\",\"C_AREA_CODE\":\"AA\",\"C_CNTY_CODE1\":\"AAA\",\"C_CNTY_CODE2\":\"232\"}]}]}");
		System.out.println("ResponseMessage:"
				+ ((super.getReq().getFirstBodyService().getServiceId().equals("eloan-accrue")) ? accRes.getJsonString()
						: testRes.getJsonString()));
		return (super.getReq().getFirstBodyService().getServiceId().equals("eloan-accrue")) ? accRes : testRes;
	}

}
