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
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.form.AccountEntryForm;
import com.example.examPlatform.form.AccountUpdForm;
import com.example.examPlatform.form.AccountUpdPassForm;
import com.example.examPlatform.form.AccountWithdrawForm;
import com.example.examPlatform.service.AccountService;
import com.example.examPlatform.validator.AccountEntryValidator;
import com.example.examPlatform.validator.AccountUpdPassValidator;
import com.example.examPlatform.validator.AccountUpdValidator;

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
	
	@Autowired
	AccountUpdValidator accountUpdValidator;
	
	@Autowired
	AccountUpdPassValidator accountUpdPassValidator;
	
	/** チェック登録 */
	@InitBinder("accountEntryForm")
	public void initEntryFormBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(accountEntryValidator);
	}
	
	/** チェック登録 */
	@InitBinder("accountUpdForm")
	public void initUpdFormBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(accountUpdValidator);
	}
	
	/** チェック登録 */
	@InitBinder("accountUpdPassForm")
	public void initUpdPassFormBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(accountUpdPassValidator);
	}
	
	/** ユーザ登録フォームの初期化 */
	@ModelAttribute
	public AccountEntryForm setUpAccountEntryForm() {
		return new AccountEntryForm();
	}
	
	/** パスワード更新フォームの初期化 */
	@ModelAttribute
	public AccountUpdPassForm setUpAccountUpdPassForm() {
		return new AccountUpdPassForm();
	}
	
	/** アカウント更新フォームの初期化 
	 * @throws NotFoundException */
	@ModelAttribute
	public AccountUpdForm setUpAccountUpdForm() throws NotFoundException {
		AccountUpdForm updForm = new AccountUpdForm();
		
		String userName = accountService.selectLoginUserName();
		Account user = accountService.selectAccountByUserName(userName);
		updForm.setUserName(user.getUserName());
		updForm.setProfile(user.getProfile());
		updForm.setUseInfoDefault(user.getUseInfoDefault());
		
		return updForm;
	}
	
	/** ユーザ退会フォームの初期化 */
	public AccountWithdrawForm setUpAccountWithdrawForm() {
		return new AccountWithdrawForm();
	}
	
/* ======================================================================= */
	
	/** ユーザ登録ページ　*/
	@GetMapping("/Entry")
	public String AccountRegisterView() {
		return "accountRegister";
	}
	
	/** ユーザ登録処理　*/
	@PostMapping("/Entry")
	public String AccountRegister(@Validated AccountEntryForm entryForm, BindingResult bindingResult, Model model,
			HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return AccountRegisterView();
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
	
	/** アカウント更新画面表示 */
	@GetMapping("/UpdAccount")
	public String AccountUpdView() {
		return "accountUpd";
	}
	
	/** アカウント更新処理 */
	@PostMapping("/UpdAccount")
	public String AccountUpd(@Validated AccountUpdForm updForm, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) return AccountUpdPassView();
		String userName = accountService.selectLoginUserName();
		
		try {
			Account user = makeAccountByUpdFrom(userName, updForm);
			accountService.userInfoUpd(user);
		} catch (NotFoundException e) {
			// ユーザ情報が見つからない場合エラーページに遷移
			model.addAttribute("errorMsg", "ユーザが見つかりませんでした");
			return "error";
		}
		
		model.addAttribute("msg", "アカウントを更新しました");
		return AccountUpdView();
	}
	
	private Account makeAccountByUpdFrom(String userName, AccountUpdForm updForm) throws NotFoundException {
		Account user = accountService.selectAccountByUserName(userName);
		user.setUserName(updForm.getUserName());
		user.setProfile(updForm.getProfile());
		user.setUseInfoDefault(updForm.getUseInfoDefault());
		return user;
	}
	
	/** パスワード更新画面表示 */
	@GetMapping("/UpdAccount/Password")
	public String AccountUpdPassView() {
		return "accountUpdPass";
	}
	
	/** パスワード更新処理 */
	@PostMapping("/UpdAccount/Password")
	public String AccountUpdPass(@Validated AccountUpdPassForm updPassForm, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) return AccountUpdPassView();
		String userName = accountService.selectLoginUserName();
		
		try {
			Integer userId = accountService.selectAccountByUserName(userName).getUserId();
			accountService.userPassUpd(userId, updPassForm.getNewPassword(), updPassForm.getOldPassword());
		} catch (NotFoundException e) {
			// ユーザ情報が見つからない場合エラーページに遷移
			model.addAttribute("errorMsg", "ユーザが見つかりませんでした");
			return "error";
		}
		
		model.addAttribute("msg", "パスワードを更新しました");
		return AccountUpdPassView();
	}
	
	/** ユーザ退会画面表示 */
	@GetMapping("/Withdraw")
	public String AccountWithdrawView() {
		return "accountWithdraw";
	}
	
	/** ユーザ退会処理 */
	@PostMapping("/Withdraw")
	public String AccountWithdraw(@Validated AccountWithdrawForm withdrawForm, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) return AccountWithdrawView();
		String userName = accountService.selectLoginUserName();
		
		boolean isWithdraw = false;
		try {
			Integer userId = accountService.selectAccountByUserName(userName).getUserId();
			isWithdraw = accountService.userWithdrow(userId, withdrawForm.getPassword());
		} catch (NotFoundException e) {
			// ユーザ情報が見つからない場合エラーページに遷移
			model.addAttribute("errorMsg", "ユーザが見つかりませんでした");
			return "error";
		}
		
		if(!isWithdraw) {
			model.addAttribute("errorMsg", "パスワードが正しくありません");
			return AccountWithdrawView();
		}
		
		return "accountWithdrawConfirm";
	}
}
