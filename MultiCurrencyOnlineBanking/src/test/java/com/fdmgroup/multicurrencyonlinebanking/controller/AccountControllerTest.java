package com.fdmgroup.multicurrencyonlinebanking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;


import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountCurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountService;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountTypeService;
import com.fdmgroup.multicurrencyonlinebanking.service.CurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.service.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
	
	@Autowired
	private MockMvc mvc;
	@MockBean
	private MockHttpSession session;
	@Autowired
	private AccountController accountController;
	@MockBean
	private CustomerService customerService;
	@MockBean
	private AccountService accountService;
	@MockBean
	private AccountTypeService accountTypeService;
	@MockBean
	private CurrencyService currencyService;
	@MockBean
	private AccountCurrencyService accountCurrencyService;
	
	@Test
	@DisplayName("If user is not logged in, he will not see the account page")
	void test_ShowAccountOverview_Redirect() throws Exception {
		when(session.getAttribute("username")).thenReturn(null);
		mvc.perform(MockMvcRequestBuilders.get("/account").session(session))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	
	@Test
	@DisplayName("If user is logged in, he will see the account page")
	void test_ShowAccountOverview() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/account").sessionAttr("username", "lujia"))
		.andExpect(MockMvcResultMatchers.view().name("account"));
	}
	
	@Test
	@DisplayName("If user is not logged in, he will not see the create account page")
	void test_showOpenAccount_Redirect() throws Exception {
		when(session.getAttribute("username")).thenReturn(null);
		mvc.perform(MockMvcRequestBuilders.get("/new-account").session(session))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	
	@Test
	@DisplayName("If user is logged in, he will see the create account page")
	void test_showOpenAccount() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/new-account").sessionAttr("username", "lujia"))
		.andExpect(MockMvcResultMatchers.view().name("new-account"));
	}
	
	@Test
	@DisplayName("If user enter an invalid amount as initial deposit, he will see an error on the same page")
	void test_processNewAccount_InvalidAmount() throws Exception {
		when(accountService.validAmount(any())).thenReturn(false);
		mvc.perform(MockMvcRequestBuilders.post("/new-account"))
		.andExpect(MockMvcResultMatchers.view().name("/new-account"))
		.andExpect(content().string(containsString("Please enter a valid amount")));
	}

	@Test
	@DisplayName("If user enter a valid amount as initial deposit, he will be redirected the the account overview page")
	void test_processNewAccount() throws Exception {
		when(accountService.validAmount(any())).thenReturn(true);
		mvc.perform(MockMvcRequestBuilders.post("/new-account"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/account?new-account=success"));
	}
	
	@Test
	@DisplayName("If user is not logged in, he will not see the create new currency page")
	void test_showAddCurrency_Redirect() throws Exception {
		when(session.getAttribute("username")).thenReturn(null);
		mvc.perform(MockMvcRequestBuilders.get("/new-currency").session(session))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	
	@Test
	@DisplayName("If user is logged in, he will see the new currency page")
	void test_showAddCurrency() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/new-currency").sessionAttr("username", "lujia"))
		.andExpect(MockMvcResultMatchers.view().name("new-currency"));
	}
	
	@Test
	@DisplayName("After user added a new currency, he will be redirected to the account overview page")
	void test_processNewCurrency() throws Exception {
		when(accountService.getAccountByAccountId(100L)).thenReturn(new Account());
		when(currencyService.getCurrencyById(100)).thenReturn(new Currency());
		when(accountCurrencyService.accountHasCurrency(any(Account.class), any(Currency.class))).thenReturn(false);
		mvc.perform(MockMvcRequestBuilders.post("/new-currency"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/account?new-currency=success"));
	}

}
