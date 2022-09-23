package com.fdmgroup.multicurrencyonlinebanking.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;

@Repository
public interface AccountCurrencyRepo extends JpaRepository<AccountCurrency, Long> {

	@Query(value = "select ac.* from account_currency ac inner join account a on "
			+ "ac.fk_account_id = a.account_id where a.account_number = ?1", nativeQuery = true)
	List<AccountCurrency> findByAccountNumber(String accountNumber);

	@Query(value = "select balance from account_currency where account_currency_id = ?1", nativeQuery = true)
	double findBalanceById(long accountCurrencyId);
	
    @Transactional
	@Modifying
	@Query(value = "update account_currency set balance = ?1 where account_currency_id = ?2", nativeQuery = true)
	void updateBalance(double newBalance, long accountCurrencyId);

    @Query(value = "select ac.* from account_currency ac inner join account a on "
    		+ "ac.fk_account_id = a.account_id inner join customer c on c.customer_id = a.fk_customer_id where c.username = ?1",
    		nativeQuery = true)
	List<AccountCurrency> findByUsername(String username);
	
}
