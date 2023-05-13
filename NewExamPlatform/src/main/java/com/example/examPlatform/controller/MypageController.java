package com.example.examPlatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	/** マイページ上での表示件数 */
	private final int displayCnt = 20;
	
	@Autowired
	MypageService mypageService;
	
	/** マイページ・ユーザページを表示 */
	@GetMapping
	public String Mypage(@PathVariable String userName, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String loginUserName = auth.getName();
		Account user;
		try {
			user = mypageService.selectUser(userName);
		} catch (NotFoundException e) {
			// ユーザ情報が見つからない場合エラーページに遷移
			return "error";
		}
		AccountView userView = new AccountView();
		userView.makeAccountView(user);
		
		List<ExamLinkView> createExamList = mypageService.selectCreateExams(userName);
		if(createExamList.size() > displayCnt) createExamList = createExamList.subList(0, displayCnt);
		
		model.addAttribute("user", userView);
		model.addAttribute("createExamList", createExamList);
		
		if(userName.equals(loginUserName)) {
			// ログインユーザ本人のマイページを表示する場合
			List<ExamLinkView> bookmarkExamList = mypageService.selectBookmarkExams(loginUserName);
			List<ReportLinkView> reportList = mypageService.selectReports(loginUserName);
			
			if(bookmarkExamList.size() > displayCnt) bookmarkExamList = bookmarkExamList.subList(0, displayCnt);
			if(reportList.size() > displayCnt) reportList = reportList.subList(0, displayCnt);
			
			model.addAttribute("bookmarkExamList", bookmarkExamList);
			model.addAttribute("reportList", reportList);
		}
		
		return "mypage";
	}
	
	/** 作成試験一覧を表示 */
	@GetMapping("Exam")
	public String MyCreateExam(@PathVariable String userName, Model model) {
		
		return "myCreateExam";
	}
}
