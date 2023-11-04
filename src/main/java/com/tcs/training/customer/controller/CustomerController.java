package com.tcs.training.customer.controller;

import com.tcs.training.customer.entity.Customer;
import com.tcs.training.customer.feign.model.HotelListings;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping("/home")
	public String homePage(Model model) {
		return "index";
	}

	@GetMapping("/signup")
	public String showRegistrationForm(Model model) {
		CustomerDTO user = new CustomerDTO();
		model.addAttribute("user", user);
		model.addAttribute("fragmentName", "registration");
		return "index";
	}

	@PostMapping("/signup/save")
	public String processSignUp(@Valid @ModelAttribute("user") CustomerDTO user, BindingResult result, Model model) {
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			result.rejectValue("password", null, "Entered passwords do not match.");
		}
		Customer existing = customerService.findByEmailAddress(user.getEmailAddress());
		if (existing != null) {
			result.rejectValue("emailAddress", null, "There is already an account registered with that email");
		}
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "index";
		}
		Customer newCustomer = new Customer();
		BeanUtils.copyProperties(user, newCustomer);
		newCustomer.setPassword(new BCryptPasswordEncoder().encode(newCustomer.getPassword()));
		customerService.add(newCustomer);
		customerService.notifyCustomerForSignUp(newCustomer);
		return "redirect:/customers/signup?success";
	}

	@GetMapping("/rooms")
	public String getRoomListings(Model model) {
		model.addAttribute("listings", customerService.getListings());
		model.addAttribute("fragmentName", "rooms");
		return "index";
	}

}
