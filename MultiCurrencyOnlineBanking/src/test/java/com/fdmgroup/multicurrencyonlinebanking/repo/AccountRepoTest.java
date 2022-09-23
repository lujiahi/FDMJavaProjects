package com.fdmgroup.multicurrencyonlinebanking.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Branch;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.util.Encryptor;


@DataJpaTest
class AccountRepoTest {
	
	@Autowired
	AccountRepo accountRepo;
	
	@Autowired
	BranchRepo branchRepo;
	
	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	AccountTypeRepo accountTypeRepo;
	
	@Test
	void test_findAccountsByUsername() {
		Branch main = new Branch("001", "Main Branch");
		AccountType savings = new AccountType("Savings", "Savings Account");
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);

		branchRepo.save(main);
		accountTypeRepo.save(savings);
		customerRepo.save(abcBakery);
		accountRepo.save(abcBakeryAccount);
		
		List<Account> expectedList = List.of(abcBakeryAccount);
		List<Account> actualList = accountRepo.findAccountsByUsername("abcbakery");
		assertEquals(expectedList, actualList);

	}

}
