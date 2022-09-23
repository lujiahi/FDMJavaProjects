package com.fdmgroup.multicurrencyonlinebanking.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Branch;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountCurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountService;
import com.fdmgroup.multicurrencyonlinebanking.service.CurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.util.Encryptor;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private MockHttpSession session;
	@MockBean
	private MockHttpServletRequest request;
	@MockBean
	private AccountService accountService;
	@MockBean
	private AccountCurrencyService accountCurrencyService;
	@MockBean
	private CurrencyService currencyService;
	
	@Test
	@DisplayName("If user is not logged in, he will not see the transfer to self page")
	void test_ShowTransferToSelf_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/transfer-self"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	
	@Test
	@DisplayName("If user is logged in, he can access the transfer to self page")
	void test_ShowTransferToSelf() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/transfer-self").sessionAttr("username", "validUser"))
		.andExpect(MockMvcResultMatchers.view().name("transfer-self"));
	}
	
	@Test
	@DisplayName("If from account and to account are the same, user will see an error")
	void test_ProcessTransferToSelf_SameAccount() throws Exception {
		when(accountService.validAmount(any())).thenReturn(true);
		mvc.perform(MockMvcRequestBuilders.post("/transfer-self").param("fromAccount", "12345").param("toAccount", "12345"))
		.andExpect(MockMvcResultMatchers.view().name("/transfer-self"))
		.andExpect(content().string(containsString("Please select a different destination account.")));
	}
	
	@Test
	@DisplayName("If user hits the Check FX rate button, user will stay on the same page")
	void test_ProcessTransferToSelf_CheckRate() throws Exception{
		mvc.perform(MockMvcRequestBuilders.post("/transfer-self").param("fromAccount", "12345").param("toAccount", "54321").param("checkRate", ""))
		.andExpect(MockMvcResultMatchers.view().name("/transfer-self"));
	}
	
	@Test
	@DisplayName("If user doesn't have enough balance to make the transfer, user will see an error")
	void test_ProcessTransferToSelf() throws Exception{
		when(accountService.validAmount(any())).thenReturn(true);
		when(accountService.sufficientBalance("123456789", "100")).thenReturn(false);
		when(accountService.transferToSelf(any(), any(), any())).thenReturn(new BigDecimal(100));
		mvc.perform(MockMvcRequestBuilders.post("/transfer-self").param("fromAccount", "123456789").param("toAccount", "987654321"))
		.andExpect(MockMvcResultMatchers.view().name("/transfer-self"))
		.andExpect(content().string(containsString("Insufficient balance in the debiting account")));
	}
	
	@Test
	@DisplayName("If user is not logged in, he will not see the fund transfer page")
	void test_ShowTransfer_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/transfer"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	
	@Test
	@DisplayName("If user is logged in, he can access the fund transfer page")
	void test_ShowTransfer() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/transfer").sessionAttr("username", "validUser"))
		.andExpect(MockMvcResultMatchers.view().name("transfer"));
	}
	
	@Test
	@DisplayName("If destination account doesn't exist, user will see an error")
	void test_SearchRecipientAccount_Account_Not_Exist() throws Exception {
		when(accountService.getAccountByAccountNumber(any())).thenReturn(null);
		mvc.perform(MockMvcRequestBuilders.post("/transfer").sessionAttr("username","validUser").param("recipientAccount", "123456789"))
		.andExpect(MockMvcResultMatchers.view().name("/transfer"))
		.andExpect(content().string(containsString("The destination account does not exist")));
	}
	
	@Test
	@DisplayName("If destination account exists, user will see the next page")
	void test_SearchRecipientAccount() throws Exception {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		
		when(accountService.getAccountByAccountNumber(any())).thenReturn(abcBakeryAccount);
		mvc.perform(MockMvcRequestBuilders.post("/transfer").sessionAttr("username","validUser").param("recipientAccount", "123456789"))
		.andExpect(MockMvcResultMatchers.view().name("/transfer-intermediate"));
	}
	
	@Test
	void test_ShowTransferIntermediate() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/transfer-intermediate").sessionAttr("username", "validUser"))
		.andExpect(MockMvcResultMatchers.view().name("transfer-intermediate"));
	}
	
	@Test
	@DisplayName("If user not logged in, he cannot access this page")
	void test_ProcessTransferIntermediate() throws Exception{
		mvc.perform(MockMvcRequestBuilders.get("/transfer-intermediate"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	
}
