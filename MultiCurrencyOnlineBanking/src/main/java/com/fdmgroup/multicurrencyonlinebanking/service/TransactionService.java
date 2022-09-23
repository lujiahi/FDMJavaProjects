package com.fdmgroup.multicurrencyonlinebanking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.model.Transaction;
import com.fdmgroup.multicurrencyonlinebanking.model.TransactionDetail;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.CustomerRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.TransactionDetailRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.TransactionRepo;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepo transactionRepo;
	@Autowired
	private TransactionDetailRepo transactionDetailRepo;


	public void newTransaction(AccountCurrency debitAccountCurrency, BigDecimal debitAmount, AccountCurrency creditAccountCurrency, BigDecimal creditAmount, String reference) {
		TransactionDetail transactionDetail = new TransactionDetail(LocalDateTime.now(), reference);
		Transaction debit = new Transaction(debitAmount, "debit", transactionDetail, debitAccountCurrency);
		Transaction credit = new Transaction(creditAmount, "credit", transactionDetail, creditAccountCurrency);
		transactionDetailRepo.save(transactionDetail);
		transactionRepo.save(debit);
		transactionRepo.save(credit);
	}

}
