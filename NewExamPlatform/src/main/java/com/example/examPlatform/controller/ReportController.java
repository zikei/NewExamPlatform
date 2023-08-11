package com.example.examPlatform.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.examPlatform.data.ReportView;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Report;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.exception.ResourceAccessException;
import com.example.examPlatform.service.AccountService;
import com.example.examPlatform.service.ExamService;
import com.example.examPlatform.service.ReportService;


/** ホームコントローラ */
@Controller
@RequestMapping("/ExamPlatform/Report")
public class ReportController {
	@Autowired
	ReportService repoService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ExamService examService;
	
	
	/** レポート画面表示 */
	@GetMapping("/{reportId}")
	public String ReportView(@PathVariable Integer reportId, Model model) {
		ReportView repoView;
		try {
			Report repo = findReportById(reportId, model);
			repoView = makeReportView(repo, model);
		} catch (ResourceAccessException e) {
			return "error";
		}
		
		model.addAttribute("repo", repoView);
		return "report";
	}
	
	
	/**
	 * レポートを取得する 取得できない場合メッセージを格納し例外をスロー
	 * @throws ResourceAccessException　レポートが見つからないまたは自身のレポート以外にアクセスした場合
	 */
	private Report findReportById(Integer repoId, Model model) throws ResourceAccessException{
		Report repo;
		try {
			repo = repoService.selectReport(repoId);
		} catch (NotFoundException e) {
			model.addAttribute("errorMsg", "レポートを取得できませんでした");
			throw new ResourceAccessException("Report NotFound");
		}
		if(!accountService.isLoginUser(repo.getUserId())) {
			// レポートが本人のもの以外の場合
			model.addAttribute("errorMsg", "このページは表示できません");
			throw new ResourceAccessException("Report forbidden");
		}
		return repo;
	}
	
	/** レポートView作成 
	 * @throws ResourceAccessException */
	private ReportView makeReportView(Report repo, Model model) throws ResourceAccessException {
		String userName;
		try {
			userName = accountService.selectAccountByUserId(repo.getUserId()).getUserName();
		} catch (NotFoundException e) {
			model.addAttribute("errorMsg", "レポートを取得できませんでした");
			throw new ResourceAccessException("Report User NotFound");
		}
		
		Optional<Exam> examOpt = examService.selectExamByExamId(repo.getExamId());
		
		String examName = "削除された試験";
		boolean isDetail = false;
		if(examOpt.isPresent()) {
			Exam exam = examOpt.get();
			examName = exam.getExamName();
			
			if(exam.getUpdateDate().before(repo.getExamDate())) {
				isDetail = true;
			}
		}
		
		return new ReportView(repo, examName, userName, isDetail);
	}
}