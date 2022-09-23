package com.fdmgroup.multicurrencyonlinebanking.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.Transaction;
import com.fdmgroup.multicurrencyonlinebanking.model.TransactionDetail;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountCurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountService;
import com.fdmgroup.multicurrencyonlinebanking.service.TransactionService;

@Controller
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountCurrencyService accountCurrencyService;

	@GetMapping("/account/{accountNumber}")
	public String showAccount(HttpSession session, @PathVariable String accountNumber, Model model) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}
		Account account = accountService.getAccountByAccountNumber(accountNumber);
		model.addAttribute("account", account);

		List<AccountCurrency> accountCurrencyList = accountCurrencyService.getAccountCurrencyByAccountNumber(accountNumber);
		model.addAttribute("accountCurrencyList", accountCurrencyList);
		return "transaction";
	}

}
