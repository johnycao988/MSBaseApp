/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import com.cs.cloud.message.api.MessageHeadTransaction;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Donald.Wang
 *
 */
public class MessageHeadTransactionEntity implements MessageHeadTransaction {

	private JsonNode trx = new ObjectMapper().createObjectNode();

	protected MessageHeadTransactionEntity(JsonNode trx) {
		this.trx = trx;
	}

	@Override
	public JsonNode getJsonObject() {
		return this.trx;
	}

	@Override
	public String getJsonString() {
		return getJsonObject().toString();
	}

	@Override
	public String getReferenceNo() {
		return this.trx.path(MessageConstant.MESSAGE_HEAD_TRANSACTION_REFERENCE_NO).textValue();
	}

	@Override
	public String getTransactionNo() {
		return this.trx.path(MessageConstant.MESSAGE_HEAD_TRANSACTION_TRX_NO).textValue();
	}

	@Override
	public String getUnitCode() {
		return this.trx.path(MessageConstant.MESSAGE_HEAD_TRANSACTION_UNIC_CODE).textValue();
	}

	@Override
	public String getFunctionId() {
		return this.trx.path(MessageConstant.MESSAGE_HEAD_TRANSACTION_FUNCTION_ID).textValue();
	}

}
