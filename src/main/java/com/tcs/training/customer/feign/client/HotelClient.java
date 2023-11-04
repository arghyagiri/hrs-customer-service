package com.tcs.training.customer.feign.client;

import com.tcs.training.customer.feign.exception.FeignClientErrorDecoder;
import com.tcs.training.customer.feign.model.HotelListings;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${feign.hotelClient.name}", configuration = FeignClientErrorDecoder.class, path = "/hotels")
public interface HotelClient {

	@GetMapping(value = "/listings")
	List<HotelListings> getHotelListings();

	@GetMapping("/{id}")
	public HotelListings getHotelById(@PathVariable Long id);

}
