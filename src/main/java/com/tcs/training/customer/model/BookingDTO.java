package com.tcs.training.customer.model;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

	@Valid
	@NotNull
	private Long roomId;

	private LocalDate startDate;

	private LocalDate endDate;

	private Long noOfGuests;

	private Long rent;

	private String roomType;

	private String emailAddress;

	private String password;

	private String cardNumber;

	private LocalDate expiryDate;

	private Long cvv;

}
