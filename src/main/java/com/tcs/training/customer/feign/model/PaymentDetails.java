package com.tcs.training.customer.feign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetails {

	private String cardNumber;

	private LocalDate expiryDate;

	private Long cvv;

}
