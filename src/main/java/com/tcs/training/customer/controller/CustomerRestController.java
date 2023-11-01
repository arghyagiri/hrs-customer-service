package com.tcs.training.customer.controller;

import com.tcs.training.customer.entity.Customer;
import com.tcs.training.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerRestController {

	private final CustomerService customerService;

	@GetMapping("/all-customers")
	public List<Customer> getAllRegisteredCustomers() {
		return customerService.getAll();
	}

}
