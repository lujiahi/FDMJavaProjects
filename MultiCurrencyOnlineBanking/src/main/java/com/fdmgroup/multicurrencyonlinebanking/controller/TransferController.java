package com.fdmgroup.multicurrencyonlinebanking.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.multicurrencyonlinebanking.model.Account;
import com.fdmgroup.multicurrencyonlinebanking.model.AccountCurrency;
import com.fdmgroup.multicurrencyonlinebanking.model.Currency;
import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountCurrencyService;
import com.fdmgroup.multicurrencyonlinebanking.service.AccountService;
import com.fdmgroup.multicurrencyonlinebanking.service.CurrencyService;

@Controller
public class TransferController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountCurrencyService accountCurrencyService;
	@Autowired
	private CurrencyService currencyService;

	@GetMapping("/transfer-self")
	public String showTransferToSelf(HttpSession session, Model model) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}
		populateTransferToSelf(session, model);
		return "transfer-self";
	}

	private void populateTransferToSelf(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		List<AccountCurrency> accountCurrencyList = accountCurrencyService.getAccountCurrencyByUsername(username);
		model.addAttribute("accountCurrencyList", accountCurrencyList);
	}

	@PostMapping("/transfer-self")
	public String processTransferToSelf(HttpServletRequest req, HttpSession session, Model model) {
		String fromAccount = req.getParameter("fromAccount");
		String toAccount = req.getParameter("toAccount");
		String amount = req.getParameter("amount");
		// check if amount is valid
		if(!accountService.validAmount(amount)) {
			populateTransferToSelf(session, model);
			model.addAttribute("error", "Please enter a valid amount.");
			return "/transfer-self";
		}
		// check if transferring to the same account
		if(fromAccount.equals(toAccount)) {
			populateTransferToSelf(session, model);
			model.addAttribute("error", "Please select a different destination account.");
			return "/transfer-self";
		}
		// check if the check rate button is hit
		if(req.getParameter("checkRate")!= null) {
			String fromCurrency = currencyService.getCurrencyByAccountCurrencyId(fromAccount);
			String toCurrency = currencyService.getCurrencyByAccountCurrencyId(toAccount);
			checkFXRate(model, fromCurrency, toCurrency, amount);
			populateTransferToSelf(session, model);
			return "/transfer-self";
		} else {		

			// check if the debiting account has enough balance
			if(!accountService.sufficientBalance(fromAccount, amount)) {
				populateTransferToSelf(session, model);
				model.addAttribute("error", "Insufficient balance in the debiting account.");
				return "/transfer-self";
			}

			BigDecimal creditAmount = accountService.transferToSelf(fromAccount, toAccount, amount);

			// if the credit amount is 0, FX API might have problem
			if(!creditAmount.equals(0)) {
				return "redirect:/account?trans=success";
			} else {
				populateTransferToSelf(session, model);
				model.addAttribute("error", "An error occured. Please check your account balance.");
				return "/transfer-self";
			}
		}
	}

	private void checkFXRate(Model model, String fromCurrency, String toCurrency, String amount) {
		BigDecimal convertedAmount = accountService.calculateConvertedAmount(fromCurrency, toCurrency, amount);
		model.addAttribute("amount", amount);
		model.addAttribute("convertedAmount", convertedAmount);
		model.addAttribute("fromCurrency", fromCurrency);
		model.addAttribute("toCurrency", toCurrency);
	}

	@GetMapping("/transfer")
	public String showTransfer(HttpSession session, Model model) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}
		return "transfer";
	}

	@PostMapping("/transfer")
	public String searchRecipientAccount(HttpSession session, Model model, HttpServletRequest req) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}
		String recipientAccount = req.getParameter("recipientAccount");
		Account account = accountService.getAccountByAccountNumber(recipientAccount);
		
		// check if the destination account exists
		if(account == null) {
			model.addAttribute("error", "The destination account does not exist.");
			return "/transfer";
		} else {
			prepareTransferForm(model, recipientAccount, session);
			return "/transfer-intermediate";
		}
	}

	private void prepareTransferForm(Model model, String recipientAccount, HttpSession session) {
		Account account = accountService.getAccountByAccountNumber(recipientAccount);
		model.addAttribute("recipientAccount", recipientAccount);
		String recipientName = account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName();
		model.addAttribute("recipientName", recipientName);
		List<AccountCurrency> recipientAccountCurrencyList = accountCurrencyService.getAccountCurrencyByAccountNumber(recipientAccount);
		model.addAttribute("recipientAccountCurrencyList", recipientAccountCurrencyList);
		List<AccountCurrency> myAccountCurrencyList = accountCurrencyService.getAccountCurrencyByUsername((String) session.getAttribute("username"));
		model.addAttribute("myAccountCurrencyList", myAccountCurrencyList);
	}


	@GetMapping("/transfer-intermediate")
	public String showTransferIntermediate(HttpSession session, Model model) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}
		return "transfer-intermediate";
	}

	@PostMapping("/transfer-intermediate")
	public String processTransferIntermediate(HttpSession session, Model model, HttpServletRequest req) {
		if(session.getAttribute("username") == null) {
			return "redirect:/";
		}
		String recipientAccountCurrencyId = req.getParameter("recipientAccountCurrency");
		String myAccountCurrencyId = req.getParameter("myAccountCurrency");
		String amount = req.getParameter("transferAmount");
		String reference = req.getParameter("reference");
		String recipientAccount = req.getParameter("recipientAccount");
		String recipientName = req.getParameter("recipientName");
		String recipientCurrency = currencyService.getCurrencyByAccountCurrencyId(recipientAccountCurrencyId);
		String myAccountCurrency = currencyService.getCurrencyByAccountCurrencyId(myAccountCurrencyId);
		// check if amount is valid
		if(!accountService.validAmount(amount)) {
			model.addAttribute("error", "Please enter a valid amount.");
			prepareTransferForm(model, recipientAccount, session);
			return "/transfer-intermediate";
		}
		// check if checking FX rate
		if(req.getParameter("checkRate")!= null) {
			checkFXRate(model, myAccountCurrency, recipientCurrency, amount);
			prepareTransferForm(model, recipientAccount, session);
			return "/transfer-intermediate";
		}

		// check if the debiting account has enough balance
		if(!accountService.sufficientBalance(myAccountCurrencyId, amount)) {
			model.addAttribute("error", "Insufficient balance in the debiting account.");
			prepareTransferForm(model, recipientAccount, session);
			return "/transfer-intermediate";
		}

		BigDecimal creditAmount = accountService.transfer(myAccountCurrencyId, recipientAccountCurrencyId, amount, reference);

		// prepare the transfer success page
		if(!creditAmount.equals(0)) {
			model.addAttribute("recipientName", recipientName);
			model.addAttribute("recipientAccount", recipientAccount);
			model.addAttribute("creditAmount", creditAmount);
			model.addAttribute("recipientCurrency", recipientCurrency);
			model.addAttribute("recipientAccountCurrencyId", recipientAccountCurrencyId);
			model.addAttribute("myAccountCurrencyId", myAccountCurrencyId);
			model.addAttribute("amount", amount);
			model.addAttribute("reference", reference);	
			AccountCurrency fromAccount = accountService.getAccountCurrencyById(myAccountCurrencyId);
			model.addAttribute("fromAccount", fromAccount);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			model.addAttribute("transferDate", LocalDate.now().format(dtf));
			model.addAttribute("message", "Your transaction has been completed.");
			model.addAttribute("transactionStatus", "Successful");
			return "/transfer-success";
		} else {
			model.addAttribute("error", "An error occured. Please check your account balance.");
			return "/transfer-intermediate";
		}
	}

}
