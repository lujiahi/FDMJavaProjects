package com.fdmgroup.multicurrencyonlinebanking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;

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
class AccountServiceTest {

	@Autowired
	AccountService accountService;
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
	@DisplayName("An account that's created first will take the first account number from the account number pool")
	void test_CreateAccount_FirstAccount() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		AccountType savings = new AccountType(100, "Savings", "Savings Account");
		Optional<AccountType> savingsOpt = Optional.of(savings);
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("159328044", abcBakery, savings);
		
		when(accountTypeRepo.findById(100)).thenReturn(savingsOpt);
		Account actual = accountService.createAccount(abcBakery, "100");
		assertEquals(abcBakeryAccount.getAccountNumber(), actual.getAccountNumber());
	}
	
	@Test
	@DisplayName("If the source account has $2000 and wants to debit $5000, return false")
	void test_SufficientBalance_NotEnough() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);

		assertFalse(accountService.sufficientBalance(abcBakeryAccountSgd, new BigDecimal(5000.0)));
	}
	
	@Test
	@DisplayName("If the source account has $2000 and wants to debit $1000, return true")
	void test_SufficientBalance_Enough() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);

		assertTrue(accountService.sufficientBalance(abcBakeryAccountSgd, new BigDecimal(1000.0)));
	}
	
	@Test
	@DisplayName("If two accounts have the same currency (sgd), return false")
	void test_NeedsCurrencyExchange_Sgd_Sgd() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		Account abcBakeryAccount2 = new Account("313515919", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);
		AccountCurrency abcBakeryAccountSgd2 = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount2, sgd);

		assertFalse(accountService.needsCurrencyExchange(abcBakeryAccountSgd, abcBakeryAccountSgd2));
	}
	
	@Test
	@DisplayName("If two accounts have different currencies (sgd and usd), return true")
	void test_NeedsCurrencyExchange_Sgd_Usd() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		Currency Usd = new Currency("USD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);
		AccountCurrency abcBakeryAccountUsd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, Usd);

		assertTrue(accountService.needsCurrencyExchange(abcBakeryAccountSgd, abcBakeryAccountUsd));
	}
	
	@Test
	@DisplayName("If an amount is negative, return false")
	void test_ValidAmount_Negative() {
		assertFalse(accountService.validAmount("-10"));
	}
	
	@Test
	@DisplayName("If an amount is not negative, return true")
	void test_ValidAmount() {
		assertTrue(accountService.validAmount("0"));
	}
	
	@Test
	@DisplayName("Call FXClient to retrieve the currency exchange rate")
	void test_CalculateConvertedAmount() {
		accountService.calculateConvertedAmount("SGD", "USD", "100");
		try {
			verify(fxclient).getResponse("SGD", "USD", "100");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	@Test
	@DisplayName("update balance will call the account currency repo")
	void test_updateBalance() {
		accountService.updateBalance(new BigDecimal(0), 100L);
		verify(accountCurrencyRepo).updateBalance(0, 100L);
	}
	
	@Test
	@DisplayName("Get account currency by id will call the account currency repo")
	void test_GetAccountCurrencyById() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);
		Optional<AccountCurrency> acOpt = Optional.of(abcBakeryAccountSgd);
		when(accountCurrencyRepo.findById(100L)).thenReturn(acOpt);

		assertEquals(accountService.getAccountCurrencyById("100"), abcBakeryAccountSgd);
	}
	
	@Test
	@DisplayName("Transfer will call the transaction service and pass in the correct parameters")
	void test_Transfer() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		Currency Usd = new Currency("USD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);
		Account abcBakeryAccount2 = new Account("313515717", abcBakery, savings);
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);
		AccountCurrency abcBakeryAccountSgd2 = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount2, sgd);
		Optional<AccountCurrency> acOpt1 = Optional.of(abcBakeryAccountSgd);
		Optional<AccountCurrency> acOpt2 = Optional.of(abcBakeryAccountSgd2);

		
		when(accountCurrencyRepo.findById(100L)).thenReturn(acOpt1);
		when(accountCurrencyRepo.findById(101L)).thenReturn(acOpt2);

		accountService.transfer("100", "101", "1000", "transfer");
		
		verify(transactionService).newTransaction(abcBakeryAccountSgd, accountService.roundToTwoDecimal(new BigDecimal(1000)), abcBakeryAccountSgd2, accountService.roundToTwoDecimal(new BigDecimal(1000)), "transfer");
	}
	
	@Test
	@DisplayName("Rounding 999.999 to two decimals to get 1000.00")
	void test_RoundToTwoDecimal() {
		assertEquals(new BigDecimal("1000.00"), accountService.roundToTwoDecimal(new BigDecimal("999.999")));
	}

	
	

}
