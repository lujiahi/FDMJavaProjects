package com.fdmgroup.multicurrencyonlinebanking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.multicurrencyonlinebanking.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

	Customer findByUsername(String username);
	
}
