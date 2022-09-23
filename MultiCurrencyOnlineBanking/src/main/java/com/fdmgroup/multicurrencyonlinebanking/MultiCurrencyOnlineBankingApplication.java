package com.fdmgroup.multicurrencyonlinebanking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fdmgroup.multicurrencyonlinebanking.util.ApplicationSetup;

@SpringBootApplication
public class MultiCurrencyOnlineBankingApplication {
	

	public static void main(String[] args) {

		SpringApplication.run(MultiCurrencyOnlineBankingApplication.class, args);

	}

}
