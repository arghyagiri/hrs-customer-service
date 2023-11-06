package com.tcs.training.customer.service;

import com.tcs.training.customer.entity.Customer;
import com.tcs.training.customer.feign.client.HotelClient;
import com.tcs.training.customer.feign.client.ReservationClient;
import com.tcs.training.customer.feign.model.HotelListings;
import com.tcs.training.customer.feign.model.PaymentDetails;
import com.tcs.training.customer.feign.model.Reservation;
import com.tcs.training.customer.model.BookingDTO;
import com.tcs.training.customer.model.CustomerDTO;
import com.tcs.training.customer.repository.CustomerRepository;
import com.tcs.training.customer.util.Constants;
import com.tcs.training.model.exception.NoDataFoundException;
import com.tcs.training.model.notification.NotificationContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	private final CustomerRepository customerRepository;

	private final Serde<NotificationContext> jsonSerde;

	private final HotelClient hotelClient;

	private final ReservationClient reservationClient;

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

	public List<HotelListings> getListings() {
		List<HotelListings> hotelListings = hotelClient.getHotelListings();
		hotelListings.forEach(v -> v.setRoomType(Constants.HOTEL_TYPE_MAP.get(v.getRoomType())));
		return hotelListings;
	}

	public List<Reservation> getMyBooking(CustomerDTO customerDTO) {
		Customer customer = customerRepository.findByEmailAddress(customerDTO.getEmailAddress());
		if (customer == null) {
			throw new NoDataFoundException("User not found");
		}
		else {
			if (!BCrypt.checkpw(customerDTO.getPassword(), customer.getPassword())) {
				throw new NoDataFoundException("User name or password invalid");
			}
		}
		return reservationClient.getReservationForCustomer(customer.getCustomerId());
	}

	public void notifyCustomerForSignUp(Customer customer) {
		NotificationContext nc = new NotificationContext();
		nc.setBody("Thank you!\n" + "\n"
				+ "Thanks for signing up. Welcome to our community. We are happy to have you on board.\n" + "\n"
				+ "Why don’t you follow us on [social media] as well?\n" + "\n" + "-Great Comfort Hotels");
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

	public Customer getRegisteredCustomerById(Long customerId) {
		return customerRepository.getReferenceById(customerId);
	}

	public BookingDTO getHotelById(BookingDTO booking) {
		HotelListings hotelListings = hotelClient.getHotelById(booking.getRoomId());
		booking.setRent(hotelListings.getRent());
		booking.setRoomType(Constants.HOTEL_TYPE_MAP.get(hotelListings.getRoomType()));
		return booking;
	}

	public boolean isValidCredentials(BookingDTO bookingDTO) {
		Customer customer = customerRepository.findByEmailAddress(bookingDTO.getEmailAddress());
		if (customer != null) {
			return BCrypt.checkpw(bookingDTO.getPassword(), customer.getPassword());
		}
		return false;
	}

	public Reservation makeReservation(@Valid BookingDTO bookingDTO) {
		Customer customer = customerRepository.findByEmailAddress(bookingDTO.getEmailAddress());
		Reservation reservation = Reservation.builder()
			.roomId(bookingDTO.getRoomId())
			.startDate(bookingDTO.getStartDate())
			.endDate(bookingDTO.getEndDate())
			.paymentDetails(PaymentDetails.builder()
				.cardNumber(bookingDTO.getCardNumber())
				.expiryDate(bookingDTO.getExpiryDate())
				.cvv(bookingDTO.getCvv())
				.build())
			.customerId(customer.getCustomerId())
			.build();
		return reservationClient.makeReservation(reservation);
	}

	public String cancelReservation(UUID reservationId) {
		return reservationClient.cancelReservation(reservationId);
	}

	public Reservation getReservationById(UUID reservationId) {
		return reservationClient.getReservationById(reservationId);
	}

}
