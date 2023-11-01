package com.tcs.training.customer.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

	@Valid
	@NotNull
	private String firstName;

	private String lastName;

	@Valid
	@NotNull
	private String emailAddress;

	@Valid
	@NotNull
	private String contactNumber;

	private String address1;

	private String address2;

	@Valid
	@NotNull
	private String password;

	@Valid
	@NotNull
	private String confirmPassword;

}
