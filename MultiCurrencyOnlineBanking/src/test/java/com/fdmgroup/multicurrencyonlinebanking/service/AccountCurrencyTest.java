package com.fdmgroup.multicurrencyonlinebanking.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Branch;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountCurrencyRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountTypeRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.BranchRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.CurrencyRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.CustomerRepo;
import com.fdmgroup.multicurrencyonlinebanking.util.Encryptor;

@SpringBootTest(properties = {"job.autorun.enabled=false"})
public class AccountCurrencyTest {
	@Autowired
	AccountCurrencyService accountCurrencyService;
	@MockBean
	private BranchRepo branchRepo;
	@MockBean
	private AccountRepo accountRepo;
	@MockBean
	private CustomerRepo customerRepo;
	@MockBean
	private AccountCurrencyRepo accountCurrencyRepo;
	@MockBean
	private CurrencyRepo currencyRepo;
	@MockBean
	private AccountTypeRepo accountTypeRepo;
	@MockBean
	private TransactionService transactionService;
	@MockBean
	private FXClient fxclient;
	
	@Test
	@DisplayName("If an account has a certain currency, return true")
	void test_AccountHasCurrency_True() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		Currency Usd = new Currency("USD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);
		AccountCurrency abcBakeryAccountUsd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, Usd);
		
		when(accountCurrencyRepo.findByAccountNumber(any())).thenReturn(List.of(abcBakeryAccountSgd, abcBakeryAccountUsd));
		
		assertTrue(accountCurrencyService.accountHasCurrency(abcBakeryAccount, Usd));
	}
	
	@Test
	@DisplayName("If an account does not have a certain currency, return false")
	void test_AccountHasCurrency_False() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		Currency Usd = new Currency("USD");
		Currency Gbp = new Currency("GBP");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);
		AccountCurrency abcBakeryAccountUsd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, Usd);
		
		when(accountCurrencyRepo.findByAccountNumber(any())).thenReturn(List.of(abcBakeryAccountSgd, abcBakeryAccountUsd));
		
		assertFalse(accountCurrencyService.accountHasCurrency(abcBakeryAccount, Gbp));
	}
	
	
	@Test
	@DisplayName("Add currency to account will call the account currency repo")
	void test_AddCurrencyToAccount() {
		accountCurrencyService.addCurrencyToAccount(new Account(), new Currency());
		verify(accountCurrencyRepo).save(any(AccountCurrency.class));
	}

}
