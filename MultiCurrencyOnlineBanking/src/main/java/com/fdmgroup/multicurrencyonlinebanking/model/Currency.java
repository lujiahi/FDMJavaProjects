package com.fdmgroup.multicurrencyonlinebanking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "currency_id")
	private int currencyId;
	
	@Column(name = "currency_name")
	private String currencyName;

	public Currency() {
		super();
	}

	public Currency(String currencyName) {
		super();
		this.currencyName = currencyName;
	}

	public Currency(int currencyId, String currencyName) {
		super();
		this.currencyId = currencyId;
		this.currencyName = currencyName;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Override
	public String toString() {
		return "Currency [currencyId=" + currencyId + ", currencyName=" + currencyName + "]";
	}
	
}
