package com.fdmgroup.multicurrencyonlinebanking.model;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transaction_id")
	private long transactionId;
	
	private BigDecimal amount;
	
	private String type;
	
	@ManyToOne
	@JoinColumn(name = "fk_transaction_id")
	private TransactionDetail transactionDetail;
	
	@ManyToOne
	@JoinColumn(name = "fk_account_currency_id")
	private AccountCurrency accountCurrency;

	public Transaction() {
		super();
	}

	public Transaction(BigDecimal amount, String type, TransactionDetail transactionDetail,
			AccountCurrency accountCurrency) {
		super();
		this.amount = amount;
		this.type = type;
		this.transactionDetail = transactionDetail;
		this.accountCurrency = accountCurrency;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionPartyId(long transactionId) {
		this.transactionId = transactionId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TransactionDetail getTransactionDetail() {
		return transactionDetail;
	}

	public void setTransactionDetail(TransactionDetail transactionDetail) {
		this.transactionDetail = transactionDetail;
	}

	public AccountCurrency getAccountCurrency() {
		return accountCurrency;
	}

	public void setAccountCurrency(AccountCurrency accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", amount=" + amount + ", type=" + type
				+ ", transactionDetail=" + transactionDetail + ", accountCurrency=" + accountCurrency + "]";
	}

	
	
}
	
	