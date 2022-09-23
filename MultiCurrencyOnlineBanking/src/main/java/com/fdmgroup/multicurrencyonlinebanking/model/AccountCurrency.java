package com.fdmgroup.multicurrencyonlinebanking.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "account_currency")
public class AccountCurrency {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_currency_id")
	private long accountCurrencyId;
	
	private BigDecimal balance;

	@ManyToOne
	@JoinColumn(name = "fk_account_id")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "fk_currency_id")
	private Currency currency;
	
	@OneToMany(mappedBy = "accountCurrency")
	@OrderBy("transactionDetail DESC")	// this ensures that the transaction history table is sorted by time descending
	private List<Transaction> transactions = new ArrayList<>();

	public AccountCurrency() {
		super();
	}

	public AccountCurrency(BigDecimal balance, Account account, Currency currency) {
		super();
		this.balance = balance;
		this.account = account;
		this.currency = currency;
	}

	public long getAccountCurrencyId() {
		return accountCurrencyId;
	}

	public void setAccountCurrencyId(long accountCurrencyId) {
		this.accountCurrencyId = accountCurrencyId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	@Override
	public String toString() {
		return "AccountCurrency [accountCurrencyId=" + accountCurrencyId + ", balance=" + balance + ", currency=" + currency + "]";
	}
	
	
}
