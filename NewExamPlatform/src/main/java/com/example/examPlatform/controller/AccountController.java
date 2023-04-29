package com.example.examPlatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.examPlatform.entity.Account;
import com.example.examPlatform.form.AccountEntryForm;
import com.example.examPlatform.service.AccountService;
import com.example.examPlatform.validator.AccountEntryValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

/** アカウントコントローラ */
@Controller
@RequestMapping("/ExamPlatform/Account")
public class AccountController {
	@Autowired
	AccountService accountService;
	
	@Autowired
	AccountEntryValidator accountEntryValidator;
	
	/** チェック登録 */
	@InitBinder("accountEntry")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(accountEntryValidator);
	}
	
	/** ユーザ登録フォームの初期化 */
	@ModelAttribute
	public AccountEntryForm setUpAccountEntryForm() {
		return new AccountEntryForm();
	}
	
	
	
	/** ユーザ登録ページ　*/
	@GetMapping("/Entry")
	public String AccountRegisterView() {
		return "accountRegister";
	}
	
	/** ユーザ登録処理　*/
	@PostMapping("/Entry")
	public String AccountRegister(@Validated AccountEntryForm entryForm, BindingResult bindingResult, Model model,
			HttpServletRequest request) {
		
		Account entryUser = new Account(null, entryForm.getUserName(), entryForm.getPassword(), "", true);
		accountService.userRegister(entryUser);
		
		try {
			request.login(entryForm.getUserName(), entryForm.getPassword());
		} catch (ServletException e) {
			e.printStackTrace();
			return "redirect:/ExamPlatform/Login";
		}
		
		return "redirect:/ExamPlatform/Mypage";
	}
}
