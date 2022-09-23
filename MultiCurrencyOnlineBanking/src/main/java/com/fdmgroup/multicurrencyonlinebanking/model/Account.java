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
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_id")
	private long accountId;
	
	@Column(name = "account_number", unique = true)
	@Pattern(regexp="[0-9]{9}", message="The account number should contain 9 digits")
	private String accountNumber;
	
	@ManyToOne
	@JoinColumn(name = "fk_customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "fk_account_type_id")
	private AccountType accountType;
	
	@OneToMany(mappedBy = "account")
	private List<AccountCurrency> accountCurrencyList = new ArrayList<>();
	

	public Account() {
		super();
	}
	
	public Account(Customer customer, AccountType accountType) {
		super();
		this.customer = customer;
		this.accountType = accountType;
	}

	public Account(String accountNumber, Customer customer, AccountType accountType) {
		super();
		this.accountNumber = accountNumber;
		this.customer = customer;
		this.accountType = accountType;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public List<AccountCurrency> getAccountCurrencyList() {
		return accountCurrencyList;
	}

	public void setAccountCurrencyList(List<AccountCurrency> accountCurrencyList) {
		this.accountCurrencyList = accountCurrencyList;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", accountNumber=" + accountNumber + ", customer=" + customer
				+ ", accountType=" + accountType + "]";
	}
	
	

}
