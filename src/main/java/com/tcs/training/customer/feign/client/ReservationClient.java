package com.tcs.training.customer.feign.client;

import com.tcs.training.customer.feign.exception.FeignClientErrorDecoder;
import com.tcs.training.customer.feign.model.HotelListings;
import com.tcs.training.customer.feign.model.Reservation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "${feign.reservationClient.name}", configuration = FeignClientErrorDecoder.class,
		path = "/reservations")
public interface ReservationClient {

	@PostMapping("/make-reservation")
	public Reservation makeReservation(@RequestBody Reservation reservation);

	@GetMapping("/customer/{id}")
	public List<Reservation> getReservationForCustomer(@PathVariable("id") Long customerId);

	@PostMapping("/cancel-reservation/{id}")
	public String cancelReservation(@PathVariable("id") UUID reservationId);

	@GetMapping("/{id}")
	public Reservation getReservationById(@PathVariable UUID id);

}
