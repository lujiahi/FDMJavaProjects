package com.fdmgroup.multicurrencyonlinebanking.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountCurrencyRepo;

@Service
public class AccountCurrencyService {
	
	@Autowired
	private AccountCurrencyRepo accountCurrencyRepo;
	
	
	public List<AccountCurrency> getAccountCurrencyByAccountNumber(String accountNumber){
		List<AccountCurrency> accountCurrencies = accountCurrencyRepo.findByAccountNumber(accountNumber);
		return accountCurrencies;
	}
	
	public List<AccountCurrency> getAccountCurrencyByUsername(String username){
		List<AccountCurrency> accountCurrencies = accountCurrencyRepo.findByUsername(username);
		return accountCurrencies;
	}
	
	public boolean accountHasCurrency(Account account, Currency currency) {
		List<AccountCurrency> accountCurrencyList = accountCurrencyRepo.findByAccountNumber(account.getAccountNumber());
		for(AccountCurrency accountCurrency : accountCurrencyList) {
			if(accountCurrency.getCurrency().equals(currency)) {
				return true;
			}
		}
		return false;
	}
	
	public AccountCurrency addCurrencyToAccount(Account account, Currency currency) {
		AccountCurrency accountCurrency = new AccountCurrency(new BigDecimal(0.0), account, currency);
		accountCurrencyRepo.save(accountCurrency);
		return accountCurrency;
	}
}
