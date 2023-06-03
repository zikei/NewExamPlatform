package com.example.examPlatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.examPlatform.form.ExamCreateForm;
import com.example.examPlatform.service.ExamService;
import com.example.examPlatform.validator.ExamCreateValidator;

/** 試験コントローラ */
@Controller
@RequestMapping("/ExamPlatform/Exam")
public class ExamController {
	@Autowired
	ExamService examService;
	
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

/* ======================================================================= */
	
	/** 試験概要登録ページ　*/
	@GetMapping("/Create")
	public String ExamCreateView() {
		return "examCreate";
	}
	
	/** 試験概要登録処理　*/
	@PostMapping("/Create")
	public String ExamCreate() {
		return QuestionCreateView();
	}
	
	/** 試験問題登録ページ　*/
	@GetMapping("/Create/Question")
	public String QuestionCreateView() {
		return "questionCreate";
	}
}
