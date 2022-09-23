package com.fdmgroup.multicurrencyonlinebanking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

	@Query(value = "select ac.* from account ac inner join customer c on ac.fk_customer_id = c.customer_id where c.username = ?1",
			nativeQuery = true)
	List<Account> findAccountsByUsername(String username);
	
	Account findByAccountNumber(String accountNumber);

	
}
