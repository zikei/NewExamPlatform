package com.example.examPlatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.examPlatform.data.AccountView;
import com.example.examPlatform.data.ExamLinkView;
import com.example.examPlatform.data.ReportLinkView;
import com.example.examPlatform.entity.Account;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.service.MypageService;

/** マイページコントローラ */
@Controller
@RequestMapping("/ExamPlatform/{userName}")
public class MypageController {
	@Autowired
	MypageService mypageService;
	
	/** マイページを表示 */
	@GetMapping
	public String Mypage(@PathVariable String userName, Model model) {
		Account loginUser;
		try {
			loginUser = mypageService.selectLoginUser();
		} catch (NotFoundException e) {
			// ログインしているユーザ情報が見つからない場合エラーページに遷移
			return "error";
		}
		AccountView loginUserView = new AccountView();
	    loginUserView.makeAccountView(loginUser);
		
		List<ExamLinkView> createExamList = mypageService.selectCreateExams();
		List<ExamLinkView> bookmarkExamList = mypageService.selectBookmarkExams();
		List<ReportLinkView> reportList = mypageService.selectReports();
		
		model.addAttribute("loginUser", loginUserView);
		model.addAttribute("createExamList", createExamList);
		model.addAttribute("bookmarkExamList", bookmarkExamList);
		model.addAttribute("reportList", reportList);
		
		return "mypage";
	}
}
