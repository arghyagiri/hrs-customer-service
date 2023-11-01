package com.tcs.training.customer.controller;

import com.tcs.training.customer.entity.Customer;
import com.tcs.training.customer.model.CustomerDTO;
import com.tcs.training.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping("/signup")
	public String showRegistrationForm(Model model) {
		CustomerDTO user = new CustomerDTO();
		model.addAttribute("user", user);
		return "registration";
	}

	@PostMapping("/signup/save")
	public String registration(@Valid @ModelAttribute("user") CustomerDTO user, BindingResult result, Model model) {
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			result.rejectValue("password", null, "Entered passwords do not match.");
		}
		Customer existing = customerService.findByEmailAddress(user.getEmailAddress());
		if (existing != null) {
			result.rejectValue("emailAddress", null, "There is already an account registered with that email");
		}
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "registration";
		}
		Customer newCustomer = new Customer();
		BeanUtils.copyProperties(user, newCustomer);
		newCustomer.setPassword(new BCryptPasswordEncoder().encode(newCustomer.getPassword()));
		customerService.add(newCustomer);
		customerService.notifyCustomerForSignUp(newCustomer);
		return "redirect:/signup?success";
	}

}
