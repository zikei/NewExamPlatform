package com.example.examPlatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.examPlatform.data.AccountView;
import com.example.examPlatform.data.PageView;
import com.example.examPlatform.data.link.ExamLinkView;
import com.example.examPlatform.data.link.ReportLinkView;
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
	
	/** マイページ・ユーザページを表示 
	 * @throws NotFoundException */
	@GetMapping
	public String Mypage(@PathVariable String userName, Model model) {
	    String loginUserName = mypageService.loginName();
		Account user;
		try {
			user = mypageService.selectUser(userName);
		} catch (NotFoundException e) {
			// ユーザ情報が見つからない場合エラーページに遷移
			model.addAttribute("errorMsg", "ユーザが見つかりませんでした");
			return "error";
		}
		AccountView userView = new AccountView(user);
		
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
	public String createExam(@PathVariable String userName, @RequestParam Integer page, Model model) {
		List<ExamLinkView> createExamList = mypageService.selectCreateExams(userName);
		
		PageView<ExamLinkView> createExamPage;
		if(page == null) {
			createExamPage = new PageView<ExamLinkView>(createExamList);
		}else {
			try {
				createExamPage = new PageView<ExamLinkView>(createExamList, page);
			} catch (NotFoundException e) {
				// 指定されたページが見つからないの場合エラーページに遷移
				model.addAttribute("errorMsg", "ページが見つかりません");
				return "error";
			}
		}
		
		model.addAttribute("CreateExamList", createExamPage.getPageList());
		model.addAttribute("Page", createExamPage);
		
		return "myCreateExam";
	}
	
	/** ブックマーク試験一覧を表示 */
	@GetMapping("Bookamrk")
	public String bookmarkExam(@PathVariable String userName, @RequestParam Integer page, Model model) {
		if(userName.equals(mypageService.loginName())) {
			// ログインユーザ以外のアクセスの場合エラーページに遷移
			model.addAttribute("errorMsg", "このページは表示できません");
			return "error";
		}
		
		List<ExamLinkView> bookmarkExamList = mypageService.selectBookmarkExams(userName);
		
		PageView<ExamLinkView> bookmarkExamLPage;
		if(page == null) {
			bookmarkExamLPage = new PageView<ExamLinkView>(bookmarkExamList);
		}else {
			try {
				bookmarkExamLPage = new PageView<ExamLinkView>(bookmarkExamList, page);
			} catch (NotFoundException e) {
				// 指定されたページが見つからないの場合エラーページに遷移
				model.addAttribute("errorMsg", "ページが見つかりません");
				return "error";
			}
		}
		
		model.addAttribute("BookmarkExamLPage", bookmarkExamLPage.getPageList());
		model.addAttribute("Page", bookmarkExamLPage);
		
		return "myCreateExam";
	}
	
	/** レポート一覧を表示 */
	@GetMapping("Report")
	public String MyCreateExam(@PathVariable String userName, @RequestParam Integer page, Model model) {
		if(userName.equals(mypageService.loginName())) {
			// ログインユーザ以外のアクセスの場合エラーページに遷移
			model.addAttribute("errorMsg", "このページは表示できません");
			return "error";
		}
		List<ReportLinkView> reportList = mypageService.selectReports(userName);
		
		PageView<ReportLinkView> reportPage;
		if(page == null) {
			reportPage = new PageView<ReportLinkView>(reportList);
		}else {
			try {
				reportPage = new PageView<ReportLinkView>(reportList, page);
			} catch (NotFoundException e) {
				// 指定されたページが見つからないの場合エラーページに遷移
				model.addAttribute("errorMsg", "ページが見つかりません");
				return "error";
			}
		}
		
		model.addAttribute("ReportList", reportPage.getPageList());
		model.addAttribute("Page", reportPage);
		
		return "myCreateExam";
	}
}
