package com.example.examPlatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.service.HomeService;

/** ホームコントローラ */
@Controller
@RequestMapping("/ExamPlatform")
public class HomeController {
	@Autowired
	HomeService homeService;
	
	/** ホーム画面 */
	@GetMapping(value= { "/","Home"})
	public String Home(Model model) {
		List<Exam> newExamList = homeService.selectNewArrivalsExams();
		List<Exam> ExeTopExamList = homeService.selectMonthlyExeTopExams();
		List<Exam> BookmarkTopExamList = homeService.selectBookmarkTopExams();
		
		model.addAttribute("newExamList", newExamList);
		model.addAttribute("exeTopExamList", ExeTopExamList);
		model.addAttribute("bookmarkTopExamList", BookmarkTopExamList);
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