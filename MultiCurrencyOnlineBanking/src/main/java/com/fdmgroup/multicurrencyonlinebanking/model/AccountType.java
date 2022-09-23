package com.fdmgroup.multicurrencyonlinebanking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account_type")
public class AccountType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_type_id")
	private int accountTypeId;
	
	@Column(name = "account_type_name", unique = true)
	private String accountTypeName;
	
	@Column(name = "account_type_desc")
	private String accountTypeDesc;

	public AccountType() {
		super();
	}

	public AccountType(String accountTypeName, String accountTypeDesc) {
		super();
		this.accountTypeName = accountTypeName;
		this.accountTypeDesc = accountTypeDesc;
	}

	public AccountType(int accountTypeId, String string, String string2) {
		this(string, string2);
		this.accountTypeId = accountTypeId;
	}

	public int getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(int accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	public String getAccountTypeDesc() {
		return accountTypeDesc;
	}

	public void setAccountTypeDesc(String accountTypeDesc) {
		this.accountTypeDesc = accountTypeDesc;
	}

	@Override
	public String toString() {
		return "AccountType [accountTypeId=" + accountTypeId + ", accountTypeName=" + accountTypeName
				+ ", accountTypeDesc=" + accountTypeDesc + "]";
	}

}
