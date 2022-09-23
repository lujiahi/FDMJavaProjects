package com.fdmgroup.multicurrencyonlinebanking.controller;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

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

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Branch;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountCurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountService;
import com.fdmgroup.multicurrencyonlinebanking.util.Encryptor;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
	
	@Autowired
	private MockMvc mvc;
	@MockBean
	private MockHttpSession session;
	@MockBean
	private MockHttpServletRequest request;
	@Autowired
	private TransactionController transactionController;
	@MockBean
	private AccountCurrencyService accountCurrencyService;
	@MockBean
	private AccountService accountService;
	
	@Test
	@DisplayName("If user is not logged in, he will not see the transaction history")
	void test_ShowAccount_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/account/{accountNumber}", "1234567"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	
	@Test
	@DisplayName("If user is logged in, he will see the transaction history of each account by accessing /account/accountNumber")
	void test_ShowAccount() throws Exception {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		Currency Usd = new Currency("USD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);
		AccountCurrency abcBakeryAccountUsd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, Usd);
		
		when(accountService.getAccountByAccountNumber("515313919")).thenReturn(abcBakeryAccount);
		when(accountCurrencyService.getAccountCurrencyByAccountNumber("515313919")).thenReturn(List.of(abcBakeryAccountSgd, abcBakeryAccountUsd));
		mvc.perform(MockMvcRequestBuilders.get("/account/{accountNumber}", "515313919").sessionAttr("username", "abcbakery"))
		.andExpect(MockMvcResultMatchers.view().name("transaction"));
	}

}
