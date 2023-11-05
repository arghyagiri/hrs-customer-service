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

@FeignClient(name = "${feign.reservationClient.name}", configuration = FeignClientErrorDecoder.class,
		path = "/reservations")
public interface ReservationClient {

	@PostMapping("/make-reservation")
	public Reservation makeReservation(@RequestBody Reservation reservation);

}
