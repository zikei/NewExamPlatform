package com.example.examPlatform.controller;

import java.util.Date;
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
import com.example.examPlatform.service.ExamServiceImpl;
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
		
		List<ExamLinkView> createExamList = formatList(mypageService.selectCreateExams(userName));
		
		for(int i=1;i<=20;i++) {
			createExamList.add(new ExamLinkView(i, "exam"+i));
		}
		
		model.addAttribute("user", userView);
		model.addAttribute("createExamList", createExamList);
		
		if(userName.equals(loginUserName)) {
			// ログインユーザ本人のマイページを表示する場合
			List<ExamLinkView> bookmarkExamList = formatList(mypageService.selectBookmarkExams(loginUserName));
			List<ReportLinkView> reportList = formatList(mypageService.selectReports(loginUserName));
			
			for(int i=1;i<=20;i++) {
				bookmarkExamList.add(new ExamLinkView(i, "book"+i));
				reportList.add(new ReportLinkView(new ExamServiceImpl(), i, "repo"+i, new Date()));
			}
			model.addAttribute("bookmarkExamList", bookmarkExamList);
			model.addAttribute("reportList", reportList);
			
			return "mypage";
			
		}else {
			//ログインユーザ本人以外の場合そのユーザのユーザページを表示
			return "userPage";
		}
	}
	
	/** リストのサイズを表示サイズに整形する */
	private <E> List<E> formatList(List<E> list) {
		if(list.size() > displayCnt) list = list.subList(0, displayCnt);
		return list;
	}
	
	/** 作成試験一覧を表示 */
	@GetMapping("/Exam")
	public String createExam(@PathVariable String userName, @RequestParam(value="page", defaultValue="1") Integer page, Model model) {
		List<ExamLinkView> createExamList = mypageService.selectCreateExams(userName);
		for(int i=1; i<100; i++) {
			createExamList.add(new ExamLinkView(i, "exam"+i));
		}
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
		
		model.addAttribute("userName", userName);
		model.addAttribute("createExamList", createExamPage.getPageList());
		model.addAttribute("page", createExamPage);
		
		return "createExam";
	}
	
	/** ブックマーク試験一覧を表示 */
	@GetMapping("/Bookmark")
	public String bookmarkExam(@PathVariable String userName, @RequestParam(value="page", defaultValue="1") Integer page, Model model) {
		if(!userName.equals(mypageService.loginName())) {
			// ログインユーザ以外のアクセスの場合エラーページに遷移
			model.addAttribute("errorMsg", "このページは表示できません");
			return "error";
		}
		
		List<ExamLinkView> bookmarkExamList = mypageService.selectBookmarkExams(userName);
		for(int i=1; i<100; i++) {
			bookmarkExamList.add(new ExamLinkView(i, "exam"+i));
		}
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
		
		model.addAttribute("userName", userName);
		model.addAttribute("bookmarkExamLPage", bookmarkExamLPage.getPageList());
		model.addAttribute("page", bookmarkExamLPage);
		
		return "bookmark";
	}
	
	/** レポート一覧を表示 */
	@GetMapping("/Report")
	public String MyCreateExam(@PathVariable String userName, @RequestParam(value="page", defaultValue="1") Integer page, Model model) {
		if(!userName.equals(mypageService.loginName())) {
			// ログインユーザ以外のアクセスの場合エラーページに遷移
			model.addAttribute("errorMsg", "このページは表示できません");
			return "error";
		}
		List<ReportLinkView> reportList = mypageService.selectReports(userName);
		for(int i=1; i<100; i++) {
			reportList.add(new ReportLinkView(new ExamServiceImpl(), i, "exam"+i, new Date()));
		}
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
		
		model.addAttribute("userName", userName);
		model.addAttribute("reportList", reportPage.getPageList());
		model.addAttribute("page", reportPage);
		
		return "report";
	}
}