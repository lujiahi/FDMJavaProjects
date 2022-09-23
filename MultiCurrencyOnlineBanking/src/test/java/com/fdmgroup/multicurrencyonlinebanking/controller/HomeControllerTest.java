package com.fdmgroup.multicurrencyonlinebanking.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fdmgroup.multicurrencyonlinebanking.service.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private MockHttpSession session;
	@MockBean
	private MockHttpServletRequest request;
	@Autowired
	private HomeController homeController;
	@MockBean
	private CustomerService customerService;
	
	@Test
	void test_ShowLogin() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(MockMvcResultMatchers.view().name("index"));
	}
	
	@Test
	@DisplayName("If user validation fails, he will not be able to login")
	void test_ValidateLogin_Validation_Fail() throws Exception {
		when(customerService.validateUser(any(), any())).thenReturn(false);
		mvc.perform(MockMvcRequestBuilders.post("/").param("username", "validuser").param("password", "validPass123"))
		.andExpect(MockMvcResultMatchers.view().name("index"));
	}
	
	@Test
	@DisplayName("If username or password is not valid, he will not be able to login")
	void test_ValidateLogin_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/").param("username", "123").param("password", "123"))
		.andExpect(MockMvcResultMatchers.view().name("index"));
	}
	
	@Test
	void test_LogOut() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/logout"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/?logout=success"));
	}
	
	@Test
	void test_Register() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/register"))
		.andExpect(MockMvcResultMatchers.view().name("register"));
	}
	
	@Test
	@DisplayName("If user enters the same username as an existing username, he will see an error")
	void test_ValidateRegistration_Same_Username() throws Exception {
		when(customerService.duplicateUsername(any())).thenReturn(true);
		mvc.perform(MockMvcRequestBuilders.post("/register").param("username", "validuser").param("password", "validPass123"))
		.andExpect(MockMvcResultMatchers.view().name("register"))
		.andExpect(content().string(containsString("This user ID is already taken")));
	}
	
	@Test
	@DisplayName("Successful registration will bring user back to the login page")
	void test_ValidateRegistration() throws Exception {
		when(customerService.duplicateUsername(any())).thenReturn(false);
		mvc.perform(MockMvcRequestBuilders.post("/register").param("username", "validuser").param("password", "validPass123"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/?reg=success"));
	}
	
}
