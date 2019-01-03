/**
 * 
 */
package com.cs.cloud.message.api.builder;

import com.cs.cloud.message.api.MessageHeadTransaction;

/**
 * @author Donald.Wang
 * 
 * @Description This builder is used to build
 *              <code>MessageHeadTransaction<code>.
 *
 */
public interface MessageHeadTransactionBuilder extends Builder<MessageHeadTransaction> {

	public MessageHeadTransactionBuilder setReferenceNo(String referenceNo);

	public MessageHeadTransactionBuilder setTransactionNo(String transactionNo);

	public MessageHeadTransactionBuilder setUnitCode(String unitCode);

	public MessageHeadTransactionBuilder setFunctionId(String functionId);

}
