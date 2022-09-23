package com.fdmgroup.multicurrencyonlinebanking.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.multicurrencyonlinebanking.model.Customer;
import com.fdmgroup.multicurrencyonlinebanking.service.CustomerService;
import com.fdmgroup.multicurrencyonlinebanking.util.Encryptor;

@Controller
public class HomeController {

	@Autowired
	private CustomerService customerService;

	@GetMapping("/")
	public String showLogin(Model model, HttpServletRequest req) {
		if(req.getParameter("reg")!=null && req.getParameter("reg").equals("success")) {
			model.addAttribute("message", "Registration successful! Please login to start banking.");
		}
		if(req.getParameter("logout")!=null && req.getParameter("logout").equals("success")) {
			model.addAttribute("message", "You have sucessfully logged out.");
		}
		model.addAttribute("customer", new Customer());
		return "index";
	}

	@PostMapping("/")
	public String validateLogin(@Valid Customer customer, BindingResult bindingResult, 
			@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
		// check if the fields entered are valid
		if(bindingResult.hasErrors()) {
			return "index";
		}
		// check if username and password match
		if(!customerService.validateUser(username, Encryptor.encrypt(password))) {
			model.addAttribute("error", "Your User ID or PIN is incorrect");
			return "index";
		}
		session.setAttribute("username", username);
		return "redirect:/account";
	}

	@GetMapping("/logout")
	public String logOut(HttpSession session) {
		session.invalidate();
		return "redirect:/?logout=success";
	}

	@GetMapping("/register")
	public String goToIndexPage(Model model) {
		model.addAttribute("customer", new Customer());
		return "register";
	}

	@PostMapping("/register")
	public String validateRegistration(@Valid Customer customer, BindingResult bindingResult, 
			Model model, @RequestParam String username) {
		// check if fields are valid
		if(bindingResult.hasErrors()) {
			return "register";
		}
		// check if username already taken
		if(customerService.duplicateUsername(username)) {
			model.addAttribute("error", "This user ID is already taken");
			return "register";
		}
		customerService.registerNewCustomer(customer);
		return "redirect:/?reg=success";
	}


}
