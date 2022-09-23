package com.fdmgroup.multicurrencyonlinebanking.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_id")
	private long customerId;
	
	@Column(unique = true, nullable = false)
	@Pattern(regexp="[a-zA-Z0-9]{5,10}", message="The user ID should be 5 to 10 characters long")
	private String username;
	
	@Column(nullable = false)
	@Pattern(regexp="[a-zA-Z0-9]{8,}", message="The PIN should be at least 8 characters long")
	private String password;
	
	@Column(name = "first_name", nullable = false)
	@Pattern(regexp="[a-zA-Z]{2,}", message="Please enter a valid first name")
	private String firstName;
	
	@Column(name = "last_name", nullable = false)
	@Pattern(regexp="[a-zA-Z]{2,}", message="Please enter a valid last name")
	private String lastName;
	
	@Column(nullable = false)
	@Pattern(regexp="[SsTtFfGg][0-9]{7}[a-zA-Z]", message="Please enter a valid NRIC/FIN")
	private String nric;
	
	@ManyToOne
	@JoinColumn(name = "fk_branch_id")
	private Branch branch;

	public Customer() {
		super();
	}
	
	public Customer(String username, String password, String firstName, String lastName, String nric) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nric = nric;
	}

	public Customer(String username, String password, String firstName, String lastName, String nric, Branch branch) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nric = nric;
		this.branch = branch;
	}
	
	

	public Customer(long customerId,
			@Pattern(regexp = "[a-zA-Z0-9]{5,10}", message = "The user ID should be between 5 and 10 characters long") String username,
			@Pattern(regexp = "[a-zA-Z0-9]{8,}", message = "The PIN should be at least 8 characters long") String password,
			@Pattern(regexp = "[a-zA-Z]{2,}", message = "Please enter a valid first name") String firstName,
			@Pattern(regexp = "[a-zA-Z]{2,}", message = "Please enter a valid last name") String lastName,
			@Pattern(regexp = "[Ss][0-9]{7}[a-zA-Z]", message = "Please enter a valid NRIC") String nric,
			Branch branch) {
		super();
		this.customerId = customerId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nric = nric;
		this.branch = branch;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", username=" + username + ", password=" + password
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", nric=" + nric + ", branch=" + branch + "]";
	}

}
