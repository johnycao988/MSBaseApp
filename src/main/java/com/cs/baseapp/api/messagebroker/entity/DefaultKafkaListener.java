/**
 * 
 */
package com.cs.baseapp.api.messagebroker.entity;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.cs.baseapp.api.filter.MessageFilter;
import com.cs.baseapp.api.messagebroker.BaseMessageListener;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.utils.ConfigConstant;

/**
 * @author Donald.Wang
 *
 */
public class DefaultKafkaListener extends BaseMessageListener {

	private KafkaConsumer<String, String> kafkaConsumer = null;

	public DefaultKafkaListener(String id, int maxProcessThreads, Properties prop, List<MessageFilter> filters,
			int connections, String tranformClass) {
		super(id, maxProcessThreads, prop, filters, connections, tranformClass);
	}

	@Override
	public void initialize() throws BaseAppException {
		this.kafkaConsumer = new KafkaConsumer<>(super.getProperties());
	}

	@Override
	public void start() throws BaseAppException {
		if (this.kafkaConsumer != null) {
			this.kafkaConsumer.subscribe(Arrays.asList(super.getProperty(ConfigConstant.TOPIC_NAME.getValue())));
		}
	}

	@Override
	public void stop() throws BaseAppException {
		if (this.kafkaConsumer != null) {
			this.kafkaConsumer.close();
		}
	}

	@Override
	public String receive() throws BaseAppException {
		String message = null;
		ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(2));
		if (records.iterator().hasNext()) {
			message = records.iterator().next().value();
		}
		return message;
	}

}
