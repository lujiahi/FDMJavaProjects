package com.fdmgroup.multicurrencyonlinebanking.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Branch;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.model.TransactionDetail;
import com.fdmgroup.multicurrencyonlinebanking.model.Transaction;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountCurrencyRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountTypeRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.BranchRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.CurrencyRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.CustomerRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.TransactionRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.TransactionDetailRepo;

@ConditionalOnProperty(prefix = "job.autorun", name = "enabled", havingValue = "true", matchIfMissing = true)
@Service
public class ApplicationSetup implements CommandLineRunner {

	@Autowired
	private BranchRepo branchRepo;
	@Autowired
	private CurrencyRepo currencyRepo;
	@Autowired
	private AccountTypeRepo accountTypeRepo;
	@Autowired
	private CustomerRepo customerRepo;
	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private AccountCurrencyRepo accountCurrencyRepo;
	@Autowired
	private TransactionDetailRepo transactionDetailRepo;
	@Autowired
	private TransactionRepo transactionRepo;

	@Override
    public void run(String... args) throws Exception {

		Branch main = new Branch("001", "Main Branch");
		Branch alexandra = new Branch("002", "Alexandra Branch");
		Branch orchard = new Branch("003", "Orchard Branch");

		Currency sgd = new Currency("SGD");
		Currency usd = new Currency("USD");
		Currency gbp = new Currency("GBP");
		Currency jpy = new Currency("JPY");
		Currency eur = new Currency("EUR");
		Currency aud = new Currency("AUD");


		AccountType savings = new AccountType("Savings", "Savings Account");
		AccountType checking = new AccountType("Checking", "Checking account");
		AccountType fixed = new AccountType("Fixed Deposit", "Fixed Deposit account");
		AccountType current = new AccountType("Current", "Current Account");
		
		Customer abcBakery = new Customer("abcbakery", Encryptor.encrypt("Pass12345"), "ABC", "Bakery", "S9988776A", main);
		Account abcBakeryAccount = new Account("515313919", abcBakery, savings);

		AccountCurrency abcBakeryAccountSgd = new AccountCurrency(new BigDecimal(1854.3), abcBakeryAccount, sgd);
		AccountCurrency abcBakeryAccountUsd = new AccountCurrency(new BigDecimal(71.1), abcBakeryAccount, usd);
		
		Customer jia = new Customer("lujia", Encryptor.encrypt("Pass12345"), "Jia", "Lu", "S8899770A", main);
		Account jiaAccount = new Account("313515717", jia, savings);

		AccountCurrency jiaAccountSgd = new AccountCurrency(new BigDecimal(1000.0), jiaAccount, sgd);
		
		Customer mike = new Customer("miketan", Encryptor.encrypt("Pass12345"), "Mike", "Tan", "S7788990A", main);
		Account mikeAccount = new Account("717515313", mike, savings);

		AccountCurrency mikeAccountUsd = new AccountCurrency(new BigDecimal(1000.0), mikeAccount, usd);

		branchRepo.save(main);
		branchRepo.save(alexandra);
		branchRepo.save(orchard);

		currencyRepo.save(sgd);
		currencyRepo.save(usd);
		currencyRepo.save(gbp);
		currencyRepo.save(jpy);
		currencyRepo.save(eur);
		currencyRepo.save(aud);
		
		accountTypeRepo.save(current);
		accountTypeRepo.save(savings);
		accountTypeRepo.save(checking);
		accountTypeRepo.save(fixed);


		customerRepo.save(abcBakery);
		accountRepo.save(abcBakeryAccount);
		accountCurrencyRepo.save(abcBakeryAccountSgd);
		accountCurrencyRepo.save(abcBakeryAccountUsd);
		
		customerRepo.save(jia);
		accountRepo.save(jiaAccount);
		accountCurrencyRepo.save(jiaAccountSgd);
		
		customerRepo.save(mike);
		accountRepo.save(mikeAccount);
		accountCurrencyRepo.save(mikeAccountUsd);
			
		TransactionDetail buyCake = new TransactionDetail(LocalDateTime.of(2022, Month.SEPTEMBER, 20, 8, 25), "Birthday Cake Order from Jia");
		Transaction buyer = new Transaction(new BigDecimal(100.0), "debit", buyCake, jiaAccountSgd);
		Transaction seller = new Transaction(new BigDecimal(100.0), "credit", buyCake, abcBakeryAccountSgd);
		
		transactionDetailRepo.save(buyCake);
		transactionRepo.save(buyer);
		transactionRepo.save(seller);
		
		TransactionDetail selfTransfer = new TransactionDetail(LocalDateTime.of(2022, Month.SEPTEMBER, 21, 11, 37), "Convert SGD to USD");
		Transaction from = new Transaction(new BigDecimal(100.0), "debit", selfTransfer, abcBakeryAccountSgd);
		Transaction to = new Transaction(new BigDecimal(71.1), "credit", selfTransfer, abcBakeryAccountUsd);
		
		transactionDetailRepo.save(selfTransfer);
		transactionRepo.save(from);
		transactionRepo.save(to);
		
		TransactionDetail buyCupCake = new TransactionDetail(LocalDateTime.of(2022, Month.SEPTEMBER, 22, 14, 3), "Cupcake Order from Mike");
		Transaction buyer2 = new Transaction(new BigDecimal(25.0), "debit", buyCupCake, mikeAccountUsd);
		Transaction seller2 = new Transaction(new BigDecimal(35.0), "credit", buyCupCake, abcBakeryAccountSgd);
		
		transactionDetailRepo.save(buyCupCake);
		transactionRepo.save(buyer2);
		transactionRepo.save(seller2);

	}

}
