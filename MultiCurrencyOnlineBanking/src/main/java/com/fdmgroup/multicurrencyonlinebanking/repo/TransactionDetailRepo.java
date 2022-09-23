package com.fdmgroup.multicurrencyonlinebanking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.multicurrencyonlinebanking.model.TransactionDetail;

@Repository
public interface TransactionDetailRepo extends JpaRepository<TransactionDetail, Long> {


	
}
