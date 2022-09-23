package com.fdmgroup.multicurrencyonlinebanking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.multicurrencyonlinebanking.model.Currency;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, Integer> {

	@Query(value = "select c.* from currency c inner join account_currency ac on c.currency_id = ac.fk_currency_id where ac.account_currency_id = ?1", nativeQuery = true)
	Currency findCurrencyByAccountCurrencyId(String accountCurrencyId);

	
}
