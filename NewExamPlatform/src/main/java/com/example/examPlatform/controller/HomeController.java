package com.example.examPlatform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** ホームコントローラ */
@Controller
@RequestMapping("/ExamPlatform")
public class HomeController {
	/** ホーム画面 */
	@GetMapping(value= { "/","Home"})
	public String Home() {
		
		
		return "home";
	}
	
	/** ログインページ */
	@GetMapping("Login")
	public String Login() {
		return "login";
	}
	
	/** 
	 * マイページリダイレクト用パス
	 * ログイン後のデフォルトページの設定にてログイン者情報を取得できなかったため作成
	 */
	@GetMapping("Mypage")
	public String MypageRedirect() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String userName = auth.getName();
	    
		return "redirect:/ExamPlatform/" + userName;
	}
}