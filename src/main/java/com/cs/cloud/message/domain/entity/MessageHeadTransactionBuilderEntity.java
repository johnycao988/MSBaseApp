/**
 * 
 */
package com.cs.cloud.message.domain.entity;

import org.apache.commons.lang3.StringUtils;

import com.cs.cloud.message.api.MessageHeadTransaction;
import com.cs.cloud.message.api.builder.MessageHeadTransactionBuilder;
import com.cs.cloud.message.domain.utils.MessageConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Donald.Wang
 *
 */
public class MessageHeadTransactionBuilderEntity implements MessageHeadTransactionBuilder {

	private ObjectNode trx = new ObjectMapper().createObjectNode();

	@Override
	public MessageHeadTransaction build() {
		return new MessageHeadTransactionEntity(this.trx);
	}

	@Override
	public MessageHeadTransactionBuilder setReferenceNo(String referenceNo) {
		if (!StringUtils.isEmpty(referenceNo)) {
			this.trx.put(MessageConstant.MESSAGE_HEAD_TRANSACTION_TRX_NO, referenceNo);
		}
		return this;
	}

	@Override
	public MessageHeadTransactionBuilder setTransactionNo(String transactionNo) {
		if (!StringUtils.isEmpty(transactionNo)) {
			this.trx.put(MessageConstant.MESSAGE_HEAD_TRANSACTION_REFERENCE_NO, transactionNo);
		}
		return this;
	}

	@Override
	public MessageHeadTransactionBuilder setUnitCode(String unitCode) {
		if (!StringUtils.isEmpty(unitCode)) {
			this.trx.put(MessageConstant.MESSAGE_HEAD_TRANSACTION_UNIC_CODE, unitCode);
		}
		return this;
	}

	@Override
	public MessageHeadTransactionBuilder setFunctionId(String functionId) {
		if (!StringUtils.isEmpty(functionId)) {
			this.trx.put(MessageConstant.MESSAGE_HEAD_TRANSACTION_FUNCTION_ID, functionId);
		}
		return this;
	}

}
