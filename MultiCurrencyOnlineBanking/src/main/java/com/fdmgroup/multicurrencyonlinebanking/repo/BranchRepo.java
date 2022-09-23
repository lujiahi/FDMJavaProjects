package com.fdmgroup.multicurrencyonlinebanking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.multicurrencyonlinebanking.model.Branch;

@Repository
public interface BranchRepo extends JpaRepository<Branch, Integer> {

	
}
