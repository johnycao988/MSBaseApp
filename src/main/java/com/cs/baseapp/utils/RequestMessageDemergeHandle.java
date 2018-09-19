/**
 * 
 */
package com.cs.baseapp.utils;

import com.cs.cloud.message.api.MessageHeadBase;
import com.cs.cloud.message.api.MessageHeadConsumer;
import com.cs.cloud.message.api.MessageHeadTransaction;

/**
 * @author Donald.Wang
 *
 */
public interface RequestMessageDemergeHandle {

	public MessageHeadBase getHeadBase(String serviceId);

	public MessageHeadConsumer getHeadConsumer(String serviceId);

	public MessageHeadTransaction getHeadTrx(String serviceId);

}
