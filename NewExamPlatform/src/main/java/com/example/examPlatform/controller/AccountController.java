package com.example.examPlatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.examPlatform.form.accountEntryForm;
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
	@InitBinder("accountEntryForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(accountEntryValidator);
	}
	
	/** ユーザ登録フォームの初期化 */
	@ModelAttribute
	public accountEntryForm setUpAccountEntryForm() {
		return new accountEntryForm();
	}
	
	
	
	/** ユーザ登録ページ　*/
	@GetMapping("/Entry")
	public String AccountRegisterView() {
		return "accountRegister";
	}
	
	/** ユーザ登録処理　*/
	@PostMapping("/Entry")
	public String AccountRegister(@Validated accountEntryForm entryForm, BindingResult bindingResult, Model model,
			HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return "accountRegister";
		}
		
		Account entryUser = new Account(null, entryForm.getUserName(), entryForm.getPassword(), "", true);
		accountService.userRegister(entryUser);
		
		//自動ログイン
		SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
		
        if (authentication instanceof AnonymousAuthenticationToken == false) {
            SecurityContextHolder.clearContext();
        }
        
		try {
			request.login(entryForm.getUserName(), entryForm.getPassword());
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
		return "redirect:/ExamPlatform/Mypage";
	}
}
