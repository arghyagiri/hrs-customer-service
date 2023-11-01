package com.tcs.training.customer.service;

import com.tcs.training.customer.entity.Customer;
import com.tcs.training.customer.repository.CustomerRepository;
import com.tcs.training.model.notification.NotificationContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	private final CustomerRepository customerRepository;

	private final Serde<NotificationContext> jsonSerde;

	@Value("${spring.cloud.stream.kafka.streams.binder.brokers}")
	private String bootstrapServer;

	public List<Customer> getAll() {
		return customerRepository.findAll();
	}

	@Transactional
	public Customer add(@RequestBody Customer customer) {
		return customerRepository.save(customer);
	}

	public Customer findByEmailAddress(String emailAddress) {
		return customerRepository.findByEmailAddress(emailAddress);
	};

	public void notifyCustomerForSignUp(Customer customer) {
		NotificationContext nc = new NotificationContext();
		nc.setBody(
				"Registration successful. You can now login to portal by using below link - http://localhost:8084/login");
		nc.setType("email");
		nc.setSeverity("Low");
		nc.setCreatedAt(new Date());
		Map<String, String> context = new HashMap<>();
		context.put("to", customer.getEmailAddress());
		context.put("sub", "Customer Registration Status");
		nc.setContext(context);
		new KafkaTemplate(orderJsonSerdeFactoryFunction.apply(jsonSerde.serializer(), bootstrapServer), true) {
			{
				setDefaultTopic("notificationProcessor");
				sendDefault(UUID.randomUUID(), nc);
			}
		};
	}

	BiFunction<Serializer<NotificationContext>, String, DefaultKafkaProducerFactory<UUID, NotificationContext>> orderJsonSerdeFactoryFunction = (
			orderSerde, bootstrapServer) -> new DefaultKafkaProducerFactory<>(
					Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer, ProducerConfig.RETRIES_CONFIG, 0,
							ProducerConfig.BATCH_SIZE_CONFIG, 16384, ProducerConfig.LINGER_MS_CONFIG, 1,
							ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432, ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
							UUIDSerializer.class, ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, orderSerde.getClass()));

}
