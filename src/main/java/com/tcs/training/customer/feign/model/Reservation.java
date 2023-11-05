package com.tcs.training.customer.feign.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Reservation {

	private String reservationId;

	private Long customerId;

	private Long roomId;

	private LocalDate startDate;

	private LocalDate endDate;

	private PaymentDetails paymentDetails;

}
