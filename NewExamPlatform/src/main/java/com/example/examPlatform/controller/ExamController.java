package com.example.examPlatform.controller;

import java.util.Date;

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

import com.example.examPlatform.data.constant.QuestionFormat;
import com.example.examPlatform.entity.Account;
import com.example.examPlatform.entity.Choices;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.form.ChoicesCreateForm;
import com.example.examPlatform.form.ExamCreateForm;
import com.example.examPlatform.service.AccountService;
import com.example.examPlatform.service.ExamService;
import com.example.examPlatform.validator.ExamCreateValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** 試験コントローラ */
@Controller
@RequestMapping("/ExamPlatform/Exam")
public class ExamController {
	@Autowired
	ExamService examService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ExamCreateValidator examCreateValidator;
	
	/** チェック登録 */
	@InitBinder("examCreateForm")
	public void initExamCreateFormBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(examCreateValidator);
	}
	
	/** ユーザ登録フォームの初期化 */
	@ModelAttribute
	public ExamCreateForm setUpExamCreateForm() {
		return new ExamCreateForm();
	}

	/** 選択肢登録フォームの初期化 */
	public ChoicesCreateForm setUpChoicesCreateForm() {
		return new ChoicesCreateForm();
	}
/* ======================================================================= */
	
	/** 試験概要登録ページ　*/
	@GetMapping("/Create")
	public String ExamCreateView() {
		return "examCreate";
	}
	
	/** 試験概要登録ページセッションタイムを延長　*/
	@PostMapping(value="/Create", params="s")
	public String ExamCreateUpdSession(ExamCreateForm examform) {
		return "examCreate";
	}
	
	/** 試験概要登録処理　*/
	@PostMapping("/Create")
	public String ExamCreate(@Validated ExamCreateForm examform, BindingResult bindingResult, Model model,
			HttpSession session, HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return ExamCreateView();
		}
		
		Account loginUser;
		try {
			loginUser = accountService.selectAccountByUserName(accountService.selectLoginUserName());
		} catch (NotFoundException e) {
			//　ログインユーザが見つからない場合ログインページに遷移
			return "redirect:/ExamPlatform/Login";
		}
		
		Exam exam = makeExam(examform, loginUser.getUserId());
		session.setAttribute("exam", exam);
		return QuestionCreateView(session);
	}
	
	/** 試験問題登録ページ　*/
	@GetMapping("/Create/Question")
	public String QuestionCreateView(HttpSession session) {
		Exam exam = (Exam) session.getAttribute("exam");
		QuestionFormat qf = new QuestionFormat();
		
		//試験概要情報が見つからない場合試験概要登録ページに遷移
		if(exam == null || exam.getQuestionFormat() == null) return ExamCreateView();
		
		//小問形式なら小問問題登録、大問形式なら大問問題登録へ遷移
		if(qf.isBigQuestionFormat(0)) {
			return "bigQuestionCreate";
		}else {
			return "questionCreate";
		}
	}
	
	
	/** formをExam形式に変換 */
	private Exam makeExam(ExamCreateForm eForm, Integer userId) {
		
		Exam exam = new Exam();
		
		exam.setGenreId(eForm.getGenreId());
		exam.setExamName(eForm.getExamName());
		exam.setPassingScore(eForm.getPassingScore());
		exam.setExamTimeMinutes(eForm.getExamTimeMinutes());
		exam.setExamExplanation(eForm.getExamExplanation());
		exam.setDisclosureRange(eForm.getDisclosureRange());
		exam.setLimitedPassword(eForm.getLimitedPassword());
		exam.setQuestionFormat(eForm.getQuestionFormat());
		
		exam.setUserId(userId);
		exam.setCreateDate(new Date());
		return exam;
	}
	
	/** 選択肢エンティティを返す */
	private Choices makeChoicesEntity(ChoicesCreateForm cForm) {
		Choices c = new Choices();
		c.setChoicesNum(cForm.getChoicesNum());
		c.setChoices(cForm.getChoices());
		return c;
	}
	
}
