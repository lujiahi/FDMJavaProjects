package com.fdmgroup.multicurrencyonlinebanking.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.repo.CurrencyRepo;


@Service
public class CurrencyService {
	
	@Autowired
	private CurrencyRepo currencyRepo;

	public List<Currency> getAllAccountTypes() {
		List<Currency> currencyList = currencyRepo.findAll();
		return currencyList;
	}
	
	public Currency getCurrencyById(int currencyId) {
		Currency currency = currencyRepo.findById(currencyId).orElseThrow(() -> new EntityNotFoundException());
		return currency;
	}
	
	public String getCurrencyByAccountCurrencyId(String accountCurrencyId) {
		Currency currency = currencyRepo.findCurrencyByAccountCurrencyId(accountCurrencyId);
		return currency.getCurrencyName();
	}

	public Currency getCurrencyById(String currencyIdToAdd) {
		return getCurrencyById(Integer.valueOf(currencyIdToAdd));
	}
}
