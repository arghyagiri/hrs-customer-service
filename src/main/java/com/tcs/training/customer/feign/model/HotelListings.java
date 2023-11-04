package com.tcs.training.customer.feign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelListings {

	private Long roomId;

	private Long rent;

	private String roomType;

	private List<Amenity> amenities;

}
