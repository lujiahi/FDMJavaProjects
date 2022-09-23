package com.fdmgroup.multicurrencyonlinebanking.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountCurrencyRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.AccountTypeRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.CustomerRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.TransactionRepo;
import com.fdmgroup.multicurrencyonlinebanking.repo.CurrencyRepo;


@Service
public class AccountService {
	
	// simulate a random account number generator...XD
	private final List<String> accountNumberPool = new ArrayList<>(List.of(
			"159328044", "956361581", "543315612", "427446964", "650308120",
			"768993619", "165286006", "155487391", "199918054", "385765035",
			"405609624", "574123446", "984742123", "706343399", "719267634",
			"388347138", "713497188", "625332992", "977950904",	"813891290"));

	private static int counter = 0;

	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private AccountCurrencyRepo accountCurrencyRepo;
	@Autowired
	private CurrencyRepo currencyRepo;
	@Autowired
	private AccountTypeRepo accountTypeRepo;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private FXClient fxclient;
	@Autowired
	private AccountCurrencyService accountCurrencyService;


	public List<Account> getAccountsByUsername(String username) {
		List<Account> accounts = accountRepo.findAccountsByUsername(username);
		return accounts;
	}

	public Account getAccountByAccountNumber(String accountNumber) {
		Account account = accountRepo.findByAccountNumber(accountNumber);
		return account;
	}
	
	public Account getAccountByAccountId(long accountId) {
		
		Account account = accountRepo.findById(accountId).orElseThrow(() -> new EntityNotFoundException());
		return account;
	}

	public AccountCurrency openAccount(Customer customer, String accountTypeId, String currencyId, String amount) {
		// find the currency
		Currency currency = currencyRepo.findById(Integer.valueOf(currencyId)).orElseThrow(() -> new EntityNotFoundException());

		// create new account
		Account account = createAccount(customer, accountTypeId);

		// create new currency wallet
		AccountCurrency accountCurrency = accountCurrencyService.addCurrencyToAccount(account, currency);

		// place an initial deposit
		if(!amount.equals("0") && !amount.equals("") ) {
			cashDeposit(accountCurrency, new BigDecimal(amount));
		}
		
		return accountCurrency;
	}
	
	public Account createAccount(Customer customer, String accountTypeId) {
		AccountType accountType = accountTypeRepo.findById(Integer.valueOf(accountTypeId)).orElseThrow(() -> new EntityNotFoundException());
		Account account = null;
		try {
			account = new Account(accountNumberPool.get(counter), customer, accountType);
			accountRepo.save(account);
			counter++;
		} catch (Exception e) {
			System.out.println("Counter exceeds limits. Please add more numbers to the account number pool");
		}
		return account;
	}
	
	public BigDecimal transferToSelf(String fromAccountCurrencyId, String toAccountCurrencyId, String debitAmount) {
		return transfer(fromAccountCurrencyId, toAccountCurrencyId, debitAmount, "Own account transfer");
	}
	
	public boolean sufficientBalance(String fromAccountCurrencyId, String amount) {
		AccountCurrency fromAccountCurrency = accountCurrencyRepo.findById(Long.valueOf(fromAccountCurrencyId)).orElseThrow(() -> new EntityNotFoundException());
		BigDecimal debitBD = roundToTwoDecimal(new BigDecimal(amount));
		return sufficientBalance(fromAccountCurrency, debitBD);
	}
	
	public boolean sufficientBalance(AccountCurrency fromAccountCurrency, BigDecimal debitAmount) {
		if(fromAccountCurrency.getBalance().compareTo(debitAmount) < 0) {
			return false;
		}
		return true;
	}
	
	public boolean needsCurrencyExchange(AccountCurrency fromAccountCurrency, AccountCurrency toAccountCurrency) {
		return !fromAccountCurrency.getCurrency().equals(toAccountCurrency.getCurrency());
	}
	
	public BigDecimal transfer(String fromAccountCurrencyId, String toAccountCurrencyId, String debitAmount, String reference) {
		AccountCurrency fromAccountCurrency = accountCurrencyRepo.findById(Long.valueOf(fromAccountCurrencyId)).orElseThrow(() -> new EntityNotFoundException());
		AccountCurrency toAccountCurrency = accountCurrencyRepo.findById(Long.valueOf(toAccountCurrencyId)).orElseThrow(() -> new EntityNotFoundException());
		BigDecimal debitBD = roundToTwoDecimal(new BigDecimal(debitAmount));
		BigDecimal creditBD = roundToTwoDecimal(new BigDecimal(0));
		
		// abort the transaction if insufficient balance
		if(!sufficientBalance(fromAccountCurrency, debitBD)){
			return creditBD;
		}
		
		// if source and destination account has the same currency, no currency exchange is needed
		if(!needsCurrencyExchange(fromAccountCurrency, toAccountCurrency)) {
			creditBD = debitBD;
		} else {
			creditBD = calculateConvertedAmount(fromAccountCurrency.getCurrency().getCurrencyName(), toAccountCurrency.getCurrency().getCurrencyName(), debitAmount);
		}
		// create a new transaction
		transactionService.newTransaction(fromAccountCurrency, debitBD, toAccountCurrency, creditBD, reference);
		
		//debit from the debit account
		debitFromAccount(fromAccountCurrency, debitBD);
		
		// credit to the credit account
		creditToAccount(toAccountCurrency, creditBD);
		return creditBD;
	}
	
	public BigDecimal calculateConvertedAmount(String fromCurrency, String toCurrency, String amount) {
		try {
			BigDecimal creditAmount = new BigDecimal(fxclient.getResponse(fromCurrency, toCurrency, amount));
			creditAmount = creditAmount.setScale(2, RoundingMode.HALF_UP);
			return creditAmount;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean validAmount(String amount) {
		if(Double.valueOf(amount) >= 0) {
			return true;
		}
		return false;
	}

	public BigDecimal roundToTwoDecimal(BigDecimal amount) {
		amount = amount.setScale(2, RoundingMode.HALF_UP);
		return amount;
	}

	public void cashDeposit(AccountCurrency accountCurrency, BigDecimal amount) {
		// assuming that the deposit always comes in cash, the transaction has no debit party (therefore null)
		transactionService.newTransaction(null, null, accountCurrency, roundToTwoDecimal(amount), "Cash Deposit");
		creditToAccount(accountCurrency, amount);
	}
	
	public void updateBalance(BigDecimal newBalance, long accountCurrencyId) {
		accountCurrencyRepo.updateBalance(newBalance.doubleValue(), accountCurrencyId);
	}
	
	public AccountCurrency getAccountCurrencyById(String accountCurrencyId) {
		AccountCurrency accountCurrency = accountCurrencyRepo.findById(Long.valueOf(accountCurrencyId)).orElseThrow(() -> new EntityNotFoundException());
		return accountCurrency;
	}

	public void creditToAccount(AccountCurrency accountCurrency, BigDecimal amount) {
		// find the current balance, add to it, and update the account balance
		long accountCurrencyId = accountCurrency.getAccountCurrencyId();
		BigDecimal currentBalance = new BigDecimal(accountCurrencyRepo.findBalanceById(accountCurrencyId));
		BigDecimal newBalance = currentBalance.add(amount);
		updateBalance(newBalance, accountCurrencyId);		
	}
	
	public void debitFromAccount(AccountCurrency accountCurrency, BigDecimal amount) {
		// find the current balance, subtract from it, and update the account balance
		long accountCurrencyId = accountCurrency.getAccountCurrencyId();
		BigDecimal currentBalance = new BigDecimal(accountCurrencyRepo.findBalanceById(accountCurrencyId));
		BigDecimal newBalance = currentBalance.subtract(amount);
		updateBalance(newBalance, accountCurrencyId);
	}

	public Account getAccountByAccountId(String accountId) {
		return getAccountByAccountId(Long.valueOf(accountId));
	}

}
