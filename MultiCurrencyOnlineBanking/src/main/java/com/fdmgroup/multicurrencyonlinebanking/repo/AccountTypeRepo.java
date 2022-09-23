package com.fdmgroup.multicurrencyonlinebanking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;

@Repository
public interface AccountTypeRepo extends JpaRepository<AccountType, Integer> {

	
}
