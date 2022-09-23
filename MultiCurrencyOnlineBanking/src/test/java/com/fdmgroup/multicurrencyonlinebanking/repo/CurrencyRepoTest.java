package com.fdmgroup.multicurrencyonlinebanking.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Branch;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.util.Encryptor;


@DataJpaTest
class CurrencyRepoTest {
	
	@Autowired
	AccountRepo accountRepo;
	
	@Autowired
	BranchRepo branchRepo;
	
	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	AccountTypeRepo accountTypeRepo;
	
	@Autowired
	CurrencyRepo currencyRepo;
	
	@Autowired
	AccountCurrencyRepo accountCurrencyRepo;
	
	@Test
	void test_findCurrencyByAccountCurrencyId() {
		Branch main = new Branch("001", "Main Branch");
		Currency sgd = new Currency("SGD");
		Currency usd = new Currency("USD");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings); // account number = 515313919
		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(2000.0), abcBakeryAccount, sgd);
		AccountCurrency abcBakeryAccountUsd = new AccountCurrency(new BigDecimal(1000.0), abcBakeryAccount, usd);	
		
		branchRepo.save(main);
		accountTypeRepo.save(savings);
		customerRepo.save(abcBakery);
		accountRepo.save(abcBakeryAccount);
		currencyRepo.save(sgd);
		currencyRepo.save(usd);
		accountCurrencyRepo.save(abcBakeryAccountSgd);
		accountCurrencyRepo.save(abcBakeryAccountUsd);
		
		Currency expected = sgd;
		Currency actual = currencyRepo.findCurrencyByAccountCurrencyId(String.valueOf(abcBakeryAccountSgd.getAccountCurrencyId()));
		assertEquals(expected, actual);

	}

}
