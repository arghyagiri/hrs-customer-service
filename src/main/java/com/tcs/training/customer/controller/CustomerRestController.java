package com.tcs.training.customer.controller;

import com.tcs.training.customer.entity.Customer;
import com.tcs.training.customer.model.CustomerDTO;
import com.tcs.training.customer.service.CustomerService;
import com.tcs.training.model.exception.NoDataFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerRestController {

	private final CustomerService customerService;

	@GetMapping("/all-customers")
	public List<Customer> getAllRegisteredCustomers() {
		return customerService.getAll();
	}

	@PostMapping("/update-profile")
	public Customer getAllRegisteredCustomers(@RequestBody CustomerDTO customer) {
		Customer existingCustomer = customerService.findByEmailAddress(customer.getEmailAddress());
		if (existingCustomer == null) {
			throw new NoDataFoundException("Invalid update request");
		}
		if (customer.getContactNumber() != null) {
			existingCustomer.setContactNumber(customer.getContactNumber());
		}
		if (customer.getAddress1() != null) {
			existingCustomer.setAddress1(customer.getAddress1());
		}
		if (customer.getAddress2() != null) {
			existingCustomer.setAddress2(customer.getAddress2());
		}
		if (customer.getFirstName() != null) {
			existingCustomer.setFirstName(customer.getFirstName());
		}
		if (customer.getLastName() != null) {
			existingCustomer.setLastName(customer.getLastName());
		}
		existingCustomer = customerService.add(existingCustomer);
		existingCustomer.setPassword(null);
		return existingCustomer;
	}

	@GetMapping("/get-customer/{id}")
	public Customer getRegisteredCustomerById(@PathVariable("id") Long customerId) {
		return customerService.getRegisteredCustomerById(customerId);
	}

	@PostMapping("/signup")
	public Customer signUp(@RequestBody CustomerDTO user) {
		Customer newCustomer = new Customer();
		BeanUtils.copyProperties(user, newCustomer);
		newCustomer.setPassword(new BCryptPasswordEncoder().encode(newCustomer.getPassword()));
		newCustomer = customerService.add(newCustomer);
		customerService.notifyCustomerForSignUp(newCustomer);
		return newCustomer;
	}

}
