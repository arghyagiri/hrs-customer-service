package com.tcs.training.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMER", uniqueConstraints = {
		@UniqueConstraint(name = "UC_CUSTOMER", columnNames = { "emailAddress", "contactNumber" }) })
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
	@SequenceGenerator(name = "customer_sequence", allocationSize = 100)
	private Long customerId;

	private String firstName;

	private String lastName;

	private String emailAddress;

	private String contactNumber;

	private String address1;

	private String address2;

	private String password;

}
