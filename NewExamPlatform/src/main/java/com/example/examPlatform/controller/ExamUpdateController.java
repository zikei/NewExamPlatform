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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.examPlatform.data.ExamData;
import com.example.examPlatform.data.constant.QuestionFormat;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Ganre;
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
@RequestMapping("/ExamPlatform/Exam/Upd")
public class ExamUpdateController {
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
	
	/** 試験概要更新ページ　*/
	@GetMapping("/Upd/{examId}")
	public String UpdExamView(@PathVariable Integer examId, ExamForm eForm, Model model) {
		Exam exam;
		try {
			exam = findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		
		setTagGanreToModel(model);
		eForm = formCtr.makeExamCreateForm(exam);
		return "updExam";
	}
	
	/** 試験概要更新ページセッションタイムを延長　*/
	@PostMapping(value="/Upd/{examId}", params="save")
	public String ExamUpdateUpdSession(@PathVariable Integer examId, ExamForm eForm, Model model) {
		// UpdExamViewを呼び足した場合フォームがリセットされるためこちらで処理を行う
		try {
			findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		setTagGanreToModel(model);
		return "updExam";
	}
	
	/** 試験概要更新ページタグ入力欄を追加　*/
	@PostMapping(value="/Upd/{examId}", params="addTag")
	public String ExamUpdateAddTag(@PathVariable Integer examId, ExamForm examform, Model model) {
		formCtr.addTag(examform);
		return ExamUpdateUpdSession(examId, examform, model);
	}
	
	/** 試験概要更新ページタグ入力欄を削除　*/
	@PostMapping(value="/Upd/{examId}", params="removeTag")
	public String ExamUpdateRemoveTag(@PathVariable Integer examId, @RequestParam Integer removeTag,
			ExamForm examform, Model model) {
		formCtr.removeTag(examform, removeTag);
		return ExamUpdateUpdSession(examId, examform, model);
	}
	
	/** 試験概要更新処理 */
	@PostMapping("/Upd/{examId}")
	public String ExamUpdate(@Validated ExamForm examform, BindingResult bindingResult, 
			@PathVariable Integer examId,  Model model) {
		if(bindingResult.hasErrors()) {
			return ExamUpdateUpdSession(examId, examform, model);
		}
		
		Exam exam;
		try {
			exam = findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		
		ExamData examData = formCtr.makeExamData(examform, exam.getUserId());
		examData.getExam().setExamId(examId);
		examService.examUpdate(examData);
		
		setTagGanreToModel(model);
		model.addAttribute("msg", "更新が完了しました");
		return "updExam";
	}
	
	/** 試験問題更新ページ　*/
	@GetMapping("/Upd/Q/{examId}")
	public String QuestionUpdateView(@PathVariable Integer examId, ExamQuestionForm eqForm, Model model) {
		Exam exam;
		try {
			exam = findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		
		ExamQuestion eq = examService.selectExamQuestionByExamId(examId);
		eqForm = formCtr.makeExamQuestionForm(eq);
		//小問形式なら小問問題更新、大問形式なら大問問題更新へ遷移
		return QuestionUpdateViewName(exam);
	}
	
	/** 試験問題更新ページセッションタイムを延長　*/
	@PostMapping(value="/Upd/Q/{examId}", params="save")
	public String QuestionUpdateUpdSession(@PathVariable Integer examId, ExamQuestionForm eqForm, Model model) {
		// QuestionUpdateViewを呼び足した場合フォームがリセットされるためこちらで処理を行う
		Exam exam;
		try {
			exam = findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		//小問形式なら小問問題更新、大問形式なら大問問題更新へ遷移
		return QuestionUpdateViewName(exam);
	}
	
	/** 試験問題更新ページ大問入力欄を追加　*/
	@PostMapping(value="/Upd/Q/{examId}", params="addBQ")
	public String QuestionUpdateAddBQ(@PathVariable Integer examId, ExamQuestionForm questionForm, Model model) {
		formCtr.addBQ(questionForm);
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ大問入力欄を削除　*/
	@PostMapping(value="/Upd/Q/{examId}", params="removeBQ")
	public String QuestionUpdateRemoveBQ(@PathVariable Integer examId, @RequestParam Integer removeBQ, 
			ExamQuestionForm questionForm, Model model) {
		formCtr.removeBQ(questionForm, removeBQ);
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ小問入力欄を追加　*/
	@PostMapping(value="/Upd/Q/{examId}", params="addQ")
	public String QuestionUpdateAddQ(@PathVariable Integer examId, @RequestParam Integer addQ,
			ExamQuestionForm questionForm, Model model) {
		formCtr.addQ(questionForm, addQ);
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ小問入力欄を削除　*/
	@PostMapping(value="/Upd/Q/{examId}", params="removeQ")
	public String QuestionUpdateRemoveQ(@PathVariable Integer examId, @RequestParam String removeQ,
			ExamQuestionForm questionForm, Model model) {
		try {
			formCtr.removeQ(questionForm, removeQ);
		}catch(NumberFormatException e) {
		}
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ選択肢入力欄を追加　*/
	@PostMapping(value="/Upd/Q/{examId}", params="addChoices")
	public String QuestionUpdateAddChoices(@PathVariable Integer examId, @RequestParam String addChoices,
			ExamQuestionForm questionForm, Model model) {
		try {
			formCtr.addC(questionForm, addChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ選択肢入力欄を削除　*/
	@PostMapping(value="/Upd/Q/{examId}", params="removeChoices")
	public String QuestionUpdateRemoveChoices(@PathVariable Integer examId, @RequestParam String removeChoices,
			ExamQuestionForm questionForm, Model model) {
		try {
			formCtr.removeC(questionForm, removeChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新処理 */
	@PostMapping("/Upd/Q/{examId}")
	public String ExamQuestionUpdate(@Validated ExamQuestionForm questionForm, BindingResult bindingResult, 
			@PathVariable Integer examId,  Model model) {
		if(bindingResult.hasErrors()) {
			return QuestionUpdateUpdSession(examId, questionForm, model);
		}
		
		Exam exam;
		try {
			exam = findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		
		ExamQuestion examQuestion = formCtr.makeExamQuestion(questionForm);
		
		examService.examQuestionUpdate(examQuestion);
		
		model.addAttribute("msg", "更新が完了しました");
		//小問形式なら小問問題更新、大問形式なら大問問題更新へ遷移
		return QuestionUpdateViewName(exam);
	}
	
/* ================================================================================================================== */
	
	/** 試験問題更新ページのビュー名を返す */
	private String QuestionUpdateViewName(Exam exam) {
		QuestionFormat qf = new QuestionFormat();
		//小問形式なら小問問題更新、大問形式なら大問問題更新へ遷移
		if(qf.isBigQuestionFormat(exam.getDisclosureRange())) {
			return "UpdBigQuestion";
		}else {
			return "UpdQuestion";
		}
	}
	
	/** タグリストとジャンルリストをModelにセット */
	public void setTagGanreToModel(Model model) {
		List<String> tagList = examService.selectTag();
		List<Ganre> ganreList = examService.selectAllGanre();
		model.addAttribute("tagList", tagList);
		model.addAttribute("ganreList", ganreList);
	}
	
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