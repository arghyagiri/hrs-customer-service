package com.tcs.training.customer.controller;

import com.tcs.training.customer.entity.Customer;
import com.tcs.training.customer.feign.model.Reservation;
import com.tcs.training.customer.model.BookingDTO;
import com.tcs.training.customer.model.CustomerDTO;
import com.tcs.training.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

	@GetMapping("/login")
	public String login(Model model) {
		CustomerDTO user = new CustomerDTO();
		model.addAttribute("user", user);
		model.addAttribute("fragmentName", "login");
		return "index";
	}

	@PostMapping("/my-booking")
	public String myBookings(@Valid @ModelAttribute("user") CustomerDTO user, BindingResult result, Model model) {
		model.addAttribute("reservations", customerService.getMyBooking(user));
		model.addAttribute("fragmentName", "my-rooms");
		return "index";
	}

	@GetMapping("/rooms/book/{id}")
	public String getBookingPage(Model model, @PathVariable("id") Long roomId) {
		model.addAttribute("booking", customerService.getHotelById(BookingDTO.builder().roomId(roomId).build()));
		model.addAttribute("fragmentName", "room-booking");
		return "index";
	}

	@PostMapping("/booking/payment")
	public String processSignUp(@Valid @ModelAttribute("booking") BookingDTO bookingDTO, BindingResult result,
			Model model) {
		model.addAttribute("booking", bookingDTO);
		if (customerService.isValidCredentials(bookingDTO)) {
			model.addAttribute("fragmentName", "payment");
		}
		else {
			return "redirect:/customers/rooms/book/" + bookingDTO.getRoomId() + "?failed";
		}
		return "index";
	}

	@PostMapping("/booking/reserve")
	public String reserveHotelRoom(@Valid @ModelAttribute("booking") BookingDTO bookingDTO, BindingResult result,
			Model model) {
		Reservation reservation = customerService.makeReservation(bookingDTO);
		bookingDTO.setReservationId(reservation.getReservationId());
		model.addAttribute("booking", bookingDTO);
		model.addAttribute("fragmentName", "room-booking-status");
		return "index";
	}

	@PostMapping("/rooms/cancel/{id}")
	public String cancelBooking(@PathVariable("id") UUID reservationId, Model model) {
		Reservation reservation = customerService.getReservationById(reservationId);
		String cancelReservation = customerService.cancelReservation(reservationId);
		model.addAttribute("booking", reservation);
		model.addAttribute("reservationMsg", cancelReservation);
		model.addAttribute("fragmentName", "room-cancel-status");
		return "index";
	}

}
