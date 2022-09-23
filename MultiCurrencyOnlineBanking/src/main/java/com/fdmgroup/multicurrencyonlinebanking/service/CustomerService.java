package com.fdmgroup.multicurrencyonlinebanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.repo.CustomerRepo;
import com.fdmgroup.multicurrencyonlinebanking.util.Encryptor;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepo customerRepo;

	public boolean registerNewCustomer(Customer customer) {
		// encrypt the password entered before saving to the DB
		customer.setPassword(Encryptor.encrypt(customer.getPassword()));
		customerRepo.save(customer);
		return true;
	}
	
	public boolean duplicateUsername(String username) {
		Customer customer = customerRepo.findByUsername(username);
		return customer != null;
	}

	public boolean validateUser(String username, String password) {
		Customer customer = customerRepo.findByUsername(username);
		if(customer != null && customer.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
	
	public Customer getCustomerByUsername(String username) {
		Customer customer = customerRepo.findByUsername(username);
		return customer;
	}
}
