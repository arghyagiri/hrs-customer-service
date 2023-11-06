package com.tcs.training.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.training.customer.model.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void customer_registration_200() throws Exception {
		CustomerDTO customerDTO = CustomerDTO.builder()
			.emailAddress("testuser@example.com")
			.firstName("test")
			.lastName("user")
			.password("test")
			.confirmPassword("test")
			.build();

		mvc.perform(MockMvcRequestBuilders.post("/customers/signup")
			.content(asJsonString(customerDTO))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.customerId").exists());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
