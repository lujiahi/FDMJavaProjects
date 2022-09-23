package com.fdmgroup.multicurrencyonlinebanking.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountType;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountCurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountService;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountTypeService;
import com.fdmgroup.multicurrencyonlinebanking.service.CurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.service.CustomerService;

@Controller
public class AccountController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountTypeService accountTypeService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private AccountCurrencyService accountCurrencyService;


	@GetMapping("/account")
	public String showAccountOverview(HttpSession session, Model model, HttpServletRequest req) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}
		if(req.getParameter("new-account")!=null && req.getParameter("new-account").equals("success")) {
			model.addAttribute("message", "New account successfully created.");
		}
		if(req.getParameter("new-currency")!=null && req.getParameter("new-currency").equals("success")) {
			model.addAttribute("message", "New currency wallet added to account.");
		}
		if(req.getParameter("trans")!=null && req.getParameter("trans").equals("success")) {
			model.addAttribute("message", "Fund transfer successful.");
		}
		String username = (String) session.getAttribute("username");
		Customer customer = customerService.getCustomerByUsername(username);
		model.addAttribute("customer", customer);
		List<Account> accounts = accountService.getAccountsByUsername(username);
		
		// check if user has any bank account
		if(accounts.size() > 0) {
			model.addAttribute("accounts", accounts);
		} else {
			model.addAttribute("createNew", "You don't have a bank account with us.");
		}
		return "account";
	}

	@GetMapping("/new-account")
	public String showOpenAccount(HttpSession session, Model model) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}

		populateOpenAccountForm(model);
		return "new-account";
	}

	@PostMapping("/new-account")
	public String processNewAccount(HttpSession session, HttpServletRequest req, Model model) {
		String accountTypeId = req.getParameter("accountType");
		String currencyId = req.getParameter("accountCurrency");
		String amount = req.getParameter("amount");
		
		// if user did not enter an amount, set amount to 0
		amount = (amount == null || amount == "") ? "0" : amount;

		String username = (String) session.getAttribute("username");
		Customer customer = customerService.getCustomerByUsername(username);
		// check if amount is valid
		if(accountService.validAmount(amount)) {
			accountService.openAccount(customer, accountTypeId, currencyId, amount);

		} else {
			populateOpenAccountForm(model);
			model.addAttribute("error", "Please enter a valid amount.");
			return "/new-account";
		}

		return "redirect:/account?new-account=success";
	}

	private void populateOpenAccountForm(Model model) {
		List<AccountType> accountTypeList = accountTypeService.getAllAccountTypes();
		List<Currency> currencyList = currencyService.getAllAccountTypes();
		model.addAttribute("accountTypeList", accountTypeList);
		model.addAttribute("currencyList", currencyList);
	}

	@GetMapping("/new-currency")
	public String showAddCurrency(HttpSession session, Model model) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}
		populateAddCurrencyForm(session, model);

		return "new-currency";
	}

	private void populateAddCurrencyForm(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		List<Currency> currencyList = currencyService.getAllAccountTypes();
		List<Account> accountList = accountService.getAccountsByUsername(username);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("accountList", accountList);
	}

	@PostMapping("/new-currency")
	public String processNewCurrency(HttpSession session, HttpServletRequest req, Model model) {

		String accountId = req.getParameter("selectAccount");
		String currencyIdToAdd = req.getParameter("addAccountCurrency");

		Account account = accountService.getAccountByAccountId(accountId);
		Currency currency = currencyService.getCurrencyById(currencyIdToAdd);
		// check if currency already exists
		if(accountCurrencyService.accountHasCurrency(account, currency)) {
			populateAddCurrencyForm(session, model);
			model.addAttribute("error", "You already have this currency wallet under the account.");
			return "/new-currency";
		} else {
			accountCurrencyService.addCurrencyToAccount(account, currency);

		}

		return "redirect:/account?new-currency=success";
	}

}
