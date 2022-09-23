package com.fdmgroup.multicurrencyonlinebanking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountCurrencyRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountTypeRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.CustomerRepo;

@Service
public class AccountTypeService {
	
	@Autowired
	private AccountTypeRepo accountTypeRepo;

	public List<AccountType> getAllAccountTypes() {
		List<AccountType> accountTypes = accountTypeRepo.findAll();
		return accountTypes;
	}
}
