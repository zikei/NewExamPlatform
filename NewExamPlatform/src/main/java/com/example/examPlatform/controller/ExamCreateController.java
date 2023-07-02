package com.example.examPlatform.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.examPlatform.data.ExamData;
import com.example.examPlatform.data.constant.QuestionFormat;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.entity.Account;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Ganre;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.form.ExamForm;
import com.example.examPlatform.form.ExamQuestionForm;
import com.example.examPlatform.service.AccountService;
import com.example.examPlatform.service.ExamFormCtrService;
import com.example.examPlatform.service.ExamService;
import com.example.examPlatform.validator.ExamValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** 試験コントローラ */
@Controller
@RequestMapping("/ExamPlatform/Exam/Create")
public class ExamCreateController {
	@Autowired
	ExamService examService;
	
	@Autowired
	ExamFormCtrService formCtr;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ExamValidator examValidator;
	
	/** チェック登録 */
	@InitBinder("examForm")
	public void initExamFormBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(examValidator);
	}
	
	/** 試験フォームの初期化 */
	@ModelAttribute
	public ExamForm setUpExamCreateForm() {
		return new ExamForm();
	}
	
	/** 試験問題フォーム初期化 */
	@ModelAttribute
	public ExamQuestionForm setUpExamQuestionCreateForm() {
		return new ExamQuestionForm();
	}

/* ================================================================================================================== */
	
	/** 試験概要登録ページ　*/
	@GetMapping
	public String ExamCreateView(Model model) {
		setTagGanreToModel(model);
		return "examCreate";
	}
	
	/** 試験概要登録ページセッションタイムを延長　*/
	@PostMapping(params="save")
	public String ExamCreateUpdSession(ExamForm examform, Model model) {
		return ExamCreateView(model);
	}
	
	/** 試験概要登録ページタグ入力欄を追加　*/
	@PostMapping(params="addTag")
	public String ExamCreateAddTag(ExamForm examform, Model model) {
		formCtr.addTag(examform);
		return ExamCreateView(model);
	}
	
	/** 試験概要登録ページタグ入力欄を削除　*/
	@PostMapping(params="removeTag")
	public String ExamCreateRemoveTag(@RequestParam Integer removeTag, ExamForm examform, Model model) {
		formCtr.removeTag(examform, removeTag);
		return ExamCreateView(model);
	}
	
	/** 試験概要処理　*/
	@PostMapping
	public String ExamCreate(@Validated ExamForm examform, BindingResult bindingResult, Model model,
			HttpSession session, HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return ExamCreateView(model);
		}
		
		Account loginUser;
		try {
			loginUser = accountService.selectAccountByUserName(accountService.selectLoginUserName());
		} catch (NotFoundException e) {
			//　ログインユーザが見つからない場合ログインページに遷移
			return "redirect:/ExamPlatform/Login";
		}
		
		ExamData exam = formCtr.makeExamData(examform, loginUser.getUserId());
		session.setAttribute("exam", exam);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ　*/
	@GetMapping("/Question")
	public String QuestionCreateView(HttpSession session, Model model) {
		ExamData examData = (ExamData) session.getAttribute("exam");
		Exam exam = examData.getExam();
		
		//試験概要情報が見つからない場合試験概要登録ページに遷移
		if(exam == null || exam.getQuestionFormat() == null) return ExamCreateView(model);
		
		//小問形式なら小問問題登録、大問形式なら大問問題登録へ遷移
		return QuestionCreateViewName(exam);
	}
	
	/** 試験問題登録ページセッションタイムを延長　*/
	@PostMapping(value="/Question", params="save")
	public String QuestionCreateUpdSession(ExamQuestionForm questionForm, HttpSession session, Model model) {
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ大問入力欄を追加　*/
	@PostMapping(value="/Question", params="addBQ")
	public String ExamCreateAddBQ(ExamQuestionForm questionForm, HttpSession session, Model model) {
		formCtr.addBQ(questionForm);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ大問入力欄を削除　*/
	@PostMapping(value="/Question", params="removeBQ")
	public String ExamCreateRemoveBQ(@RequestParam Integer removeBQ, ExamQuestionForm questionForm,
			HttpSession session, Model model) {
		formCtr.removeBQ(questionForm, removeBQ);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ小問入力欄を追加　*/
	@PostMapping(value="/Question", params="addQ")
	public String ExamCreateAddQ(@RequestParam Integer addQ, ExamQuestionForm questionForm, HttpSession session,
			Model model) {
		formCtr.addQ(questionForm, addQ);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ小問入力欄を削除　*/
	@PostMapping(value="/Question", params="removeQ")
	public String ExamCreateRemoveQ(@RequestParam String removeQ, ExamQuestionForm questionForm,
			HttpSession session, Model model) {
		try {
			formCtr.removeQ(questionForm, removeQ);
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ選択肢入力欄を追加　*/
	@PostMapping(value="/Question", params="addChoices")
	public String ExamCreateAddChoices(@RequestParam String addChoices, ExamQuestionForm questionForm,
			HttpSession session, Model model) {
		try {
			formCtr.addC(questionForm, addChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ選択肢入力欄を削除　*/
	@PostMapping(value="/Question", params="removeChoices")
	public String ExamCreateRemoveChoices(@RequestParam String removeChoices, ExamQuestionForm questionForm,
			HttpSession session, Model model) {
		try {
			formCtr.removeC(questionForm, removeChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題処理　*/
	@PostMapping("/Question")
	public String QuestionCreate(@Validated ExamQuestionForm questionForm, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return QuestionCreateView(session, model);
		}
		
		ExamQuestion examQuestion = formCtr.makeExamQuestion(questionForm);
		session.setAttribute("examQuestion", examQuestion);
		return CreateConfirmView(session, model);
	}
	
	/** 試験登録確認ページ　*/
	@GetMapping("/Confirm")
	public String CreateConfirmView(HttpSession session, Model model) {
		ExamData examData = (ExamData) session.getAttribute("exam");
		ExamQuestion examQuestion = (ExamQuestion) session.getAttribute("examQuestion");
		
		//試験概要情報が見つからない場合試験概要登録ページに遷移
		if(examData == null) return ExamCreateView(model);
		//試験問題情報が見つからない場合試験問題登録ページに遷移
		if(examQuestion == null) return QuestionCreateView(session, model);
		
		int fullScore = examQuestion.fullScore();
		model.addAttribute("fullScore", fullScore);
		return "examCreateConfirm";
	}

	/** 試験登録処理　*/
	@PostMapping("/Confirm")
	public String CreateConfirm(HttpSession session, Model model) {
		ExamData examData = (ExamData) session.getAttribute("exam");
		ExamQuestion examQuestion = (ExamQuestion) session.getAttribute("examQuestion");
		
		//試験概要情報が見つからない場合試験概要登録ページに遷移
		if(examData == null) return ExamCreateView(model);
		//試験問題情報が見つからない場合試験問題登録ページに遷移
		if(examQuestion == null) return QuestionCreateView(session, model);
		
		//合格点が満点より大きい場合確認ページに戻る
		if(examQuestion.fullScore() < examData.getExam().getPassingScore()) {
			model.addAttribute("errorMsg", "合格点が満点より大きく設定されています");
			return CreateConfirmView(session, model);
		}
		
		examService.examRegister(examData, examQuestion);
		return "redirect:/ExamPlatform/Mypage";
	}
	
/* ================================================================================================================== */
	
	/** 試験問題作成ページのビュー名を返す */
	private String QuestionCreateViewName(Exam exam) {
		QuestionFormat qf = new QuestionFormat();
		//小問形式なら小問問題登録、大問形式なら大問問題登録へ遷移
		if(qf.isBigQuestionFormat(exam.getDisclosureRange())) {
			return "bigQuestionCreate";
		}else {
			return "questionCreate";
		}
	}
	
	/** タグリストとジャンルリストをModelにセット */
	public void setTagGanreToModel(Model model) {
		List<String> tagList = examService.selectTag();
		List<Ganre> ganreList = examService.selectAllGanre();
		model.addAttribute("tagList", tagList);
		model.addAttribute("ganreList", ganreList);
	}
}