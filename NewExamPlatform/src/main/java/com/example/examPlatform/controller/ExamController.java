package com.example.examPlatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.exception.ResourceAccessException;
import com.example.examPlatform.form.ExamForm;
import com.example.examPlatform.form.ExamQuestionForm;
import com.example.examPlatform.service.AccountService;
import com.example.examPlatform.service.ExamFormCtrService;
import com.example.examPlatform.service.ExamService;
import com.example.examPlatform.validator.ExamValidator;

/** 試験コントローラ */
@Controller
@RequestMapping("/ExamPlatform/Exam")
public class ExamController {
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
	
	/** 試験削除ページ */
	@GetMapping("/Delete/{examId}")
	public String ExamDeleteView(@PathVariable Integer examId, Model model) {
		Exam exam;
		try {
			exam = findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		model.addAttribute("exam", exam);
		return "examDelete";
	}
	
	/** 試験削除処理 */
	@PostMapping("/Delete/{examId}")
	public String ExamDelete(@PathVariable Integer examId, Model model) {
		try {
			findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		
		examService.examDelete(examId);
		
		return "redirect:/ExamPlatform/Mypage";
	}
	
/* ================================================================================================================== */
	
	/** 試験を取得する 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合modelにメッセージを格納し例外をスローする*/
	private Exam findCreateExamById(Integer examId, Model model) throws ResourceAccessException{
		Exam exam;
		try {
			exam = examService.selectExamByExamId(examId).orElseThrow(() -> new NotFoundException("Exam NotFound"));
		} catch (NotFoundException e) {
			// 試験が見つからない場合
			model.addAttribute("errorMsg", "試験が見つかりません");
			throw new ResourceAccessException("Exam NotFound");
		}
		if(!accountService.isLoginUser(exam.getUserId())) {
			// ログインユーザ以外のアクセスの場合
			model.addAttribute("errorMsg", "このページは表示できません");
			throw new ResourceAccessException("Exam forbidden");
		}
		return exam;
	}
}