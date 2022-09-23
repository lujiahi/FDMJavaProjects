package com.fdmgroup.multicurrencyonlinebanking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class TransactionDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transaction_detail_id")
	private long transactionDetailId;
	
	@Column(name = "transaction_time")
	private LocalDateTime transactionTime;
	
	private String reference;
	
	public TransactionDetail() {
		super();
	}

	public TransactionDetail(LocalDateTime transactionTime, String reference) {
		super();
		this.transactionTime = transactionTime;
		this.reference = reference;
	}

	public long getTransactionDetailId() {
		return transactionDetailId;
	}

	public void setTransactionDetailId(long transactionDetailId) {
		this.transactionDetailId = transactionDetailId;
	}

	public String getTransactionTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return transactionTime.format(formatter);
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Override
	public String toString() {
		return "TransactionDetail [transactionDetailId=" + transactionDetailId + ", transactionTime=" + transactionTime
				+ ", reference=" + reference + "]";
	}


}
