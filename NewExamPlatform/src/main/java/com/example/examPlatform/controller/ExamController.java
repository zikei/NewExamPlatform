package com.example.examPlatform.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.example.examPlatform.data.question.BigQuestionData;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.data.question.QuestionData;
import com.example.examPlatform.entity.Account;
import com.example.examPlatform.entity.BigQuestion;
import com.example.examPlatform.entity.Choices;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Ganre;
import com.example.examPlatform.entity.Question;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.form.BigQuestionCreateForm;
import com.example.examPlatform.form.ChoicesCreateForm;
import com.example.examPlatform.form.ExamCreateForm;
import com.example.examPlatform.form.ExamQuestionCreateForm;
import com.example.examPlatform.form.QuestionCreateForm;
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
	
	/** 試験登録フォームの初期化 */
	@ModelAttribute
	public ExamCreateForm setUpExamCreateForm() {
		return new ExamCreateForm();
	}
	
	/** 試験問題登録フォーム初期化 */
	@ModelAttribute
	public ExamQuestionCreateForm setUpExamQuestionCreateForm() {
		return new ExamQuestionCreateForm();
	}

/* ======================================================================= */
	
	/** 試験概要登録ページ　*/
	@GetMapping("/Create")
	public String ExamCreateView(Model model) {
		List<String> tagList = examService.selectTag();
		List<Ganre> ganreList = examService.selectAllGanre();
		model.addAttribute("tagList", tagList);
		model.addAttribute("ganreList", ganreList);
		return "examCreate";
	}
	
	/** 試験概要登録ページセッションタイムを延長　*/
	@PostMapping(value="/Create", params="save")
	public String ExamCreateUpdSession(ExamCreateForm examform, Model model) {
		return ExamCreateView(model);
	}
	
	/** 試験概要登録ページタグ入力欄を追加　*/
	@PostMapping(value="/Create", params="addTag")
	public String ExamCreateAddTag(ExamCreateForm examform, Model model) {
		examform.addTag();
		return ExamCreateView(model);
	}
	
	/** 試験概要登録ページタグ入力欄を削除　*/
	@PostMapping(value="/Create", params="removeTag")
	public String ExamCreateRemoveTag(@RequestParam Integer removeTag, ExamCreateForm examform, Model model) {
		Integer tagidx = removeTag;
		examform.removeTag(tagidx);
		return ExamCreateView(model);
	}
	
	/** 試験概要処理　*/
	@PostMapping("/Create")
	public String ExamCreate(@Validated ExamCreateForm examform, BindingResult bindingResult, Model model,
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
		
		ExamData exam = makeExamData(examform, loginUser.getUserId());
		session.setAttribute("exam", exam);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ　*/
	@GetMapping("/Create/Question")
	public String QuestionCreateView(HttpSession session, Model model) {
		ExamData examData = (ExamData) session.getAttribute("exam");
		Exam exam = examData.getExam();
		QuestionFormat qf = new QuestionFormat();
		
		//試験概要情報が見つからない場合試験概要登録ページに遷移
		if(exam == null || exam.getQuestionFormat() == null) return ExamCreateView(model);
		
		//小問形式なら小問問題登録、大問形式なら大問問題登録へ遷移
		if(qf.isBigQuestionFormat(0)) {
			return "bigQuestionCreate";
		}else {
			return "questionCreate";
		}
	}
	
	/** 試験問題登録ページセッションタイムを延長　*/
	@PostMapping(value="/Create/Question", params="save")
	public String QuestionCreateUpdSession(ExamQuestionCreateForm questionForm, HttpSession session, Model model) {
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ大問入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addBQ")
	public String ExamCreateAddBQ(ExamQuestionCreateForm questionForm, HttpSession session, Model model) {
		questionForm.addBQ();
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ大問入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeBQ")
	public String ExamCreateRemoveBQ(@RequestParam Integer removeBQ, ExamQuestionCreateForm questionForm,
			HttpSession session, Model model) {
		Integer bqidx = removeBQ;
		questionForm.removeBQ(bqidx);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ小問入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addQ")
	public String ExamCreateAddQ(@RequestParam Integer addQ, ExamQuestionCreateForm questionForm, HttpSession session,
			Model model) {
		Integer bqidx = addQ;
		questionForm.addQ(bqidx);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ小問入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeQ")
	public String ExamCreateRemoveQ(@RequestParam String removeQ, ExamQuestionCreateForm questionForm,
			HttpSession session, Model model) {
		try {
			Integer[] tmp = makeIntegerArray(removeQ);
			if(tmp.length == 2) {
				Integer bqidx = tmp[0];
				Integer qidx  = tmp[1];
				questionForm.removeQ(bqidx, qidx);
			}
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ選択肢入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addChoices")
	public String ExamCreateAddChoices(@RequestParam String addChoices, ExamQuestionCreateForm questionForm,
			HttpSession session, Model model) {
		try {
			Integer[] tmp = makeIntegerArray(addChoices);
			if(tmp.length == 2) {
				Integer bqidx = tmp[0];
				Integer qidx  = tmp[1];
				questionForm.addChoices(bqidx, qidx);
			}
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ選択肢入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeChoices")
	public String ExamCreateRemoveChoices(@RequestParam String removeChoices, ExamQuestionCreateForm questionForm,
			HttpSession session, Model model) {
		try {
			Integer[] tmp = makeIntegerArray(removeChoices);
			if(tmp.length == 3) {
				Integer bqidx = tmp[0];
				Integer qidx  = tmp[1];
				Integer cidx  = tmp[2];
				questionForm.removeChoices(bqidx, qidx, cidx);
			}
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題処理　*/
	@PostMapping("/Create/Question")
	public String QuestionCreate(@Validated ExamQuestionCreateForm questionForm, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return QuestionCreateView(session, model);
		}
		
		ExamQuestion examQuestion = makeExamQuestion(questionForm);
		session.setAttribute("examQuestion", examQuestion);
		return CreateConfirmView(session, model);
	}
	
	/** 試験登録確認ページ　*/
	@GetMapping("/Create/Confirm")
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
	@PostMapping("/Create/Confirm")
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
	
	
	
	/** 文字列をスペースで分割し分割した文字列をInteger型配列に変換する */
	private Integer[] makeIntegerArray(String str) throws NumberFormatException{
		String[] arrayStr = str.split(" ");
		Integer[] arrayInt = new Integer[arrayStr.length];
		for(int i = 0; i < arrayStr.length; i++) {
			arrayInt[i] = Integer.valueOf(arrayStr[i]);
		}
		return arrayInt;
	}
	
	/** formをExamData形式に変換 */
	private ExamData makeExamData(ExamCreateForm eForm, Integer userId) {
		Exam exam = makeExam(eForm, userId);
		List<String> tagList = new ArrayList<>();
		eForm.getTagList().forEach(t -> tagList.add(t.getTag()));
		tagList.removeIf(s -> s==null || s.isBlank());
		
		ExamData examData = new ExamData(exam, tagList);
		return examData;
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
	
	/** ExamQuestionに変換 */
	private ExamQuestion makeExamQuestion(ExamQuestionCreateForm form){
		List<BigQuestionCreateForm> bqFormList = form.getBigQuestionCreateForm();
		List<BigQuestionData> bqDataList = new ArrayList<BigQuestionData>();
		
		for(BigQuestionCreateForm bqForm : bqFormList) {
			bqDataList.add(makeBigQuestionData(bqForm));
		}
		return new ExamQuestion(bqDataList);
	}
	
	/** BigQuestionDataに変換 */
	private BigQuestionData makeBigQuestionData(BigQuestionCreateForm form){
		BigQuestion bqEntity = makeBigQuestionEntity(form);
		
		List<QuestionCreateForm> qFormList = form.getQuestionCreateForm();
		List<QuestionData> qDataList = new ArrayList<QuestionData>();
			
		for(QuestionCreateForm qForm : qFormList) {
			qDataList.add(makeQuestionData(qForm));
		}
		
		return new BigQuestionData(bqEntity, qDataList);
	}
	
	/** QuestionDataに変換 */
	private QuestionData makeQuestionData(QuestionCreateForm form){
		Question qEntity = makeQuestionEntity(form);
		
		List<ChoicesCreateForm> cFormList = form.getChoicesFormList();
		List<Choices> cList = new ArrayList<Choices>();
		
		for(ChoicesCreateForm cForm : cFormList) {
			cList.add(makeChoicesEntity(cForm));
		}
		
		return new QuestionData(qEntity, cList);
	}
	
	/** 選択肢エンティティに変換 */
 	private Choices makeChoicesEntity(ChoicesCreateForm cForm) {
		Choices c = new Choices();
		c.setChoicesNum(cForm.getChoicesNum());
		c.setChoices(cForm.getChoices());
		return c;
	}
	
	/** 小問エンティティに変換 */
	private Question makeQuestionEntity(QuestionCreateForm qForm) {
		Question q = new Question();
		q.setQuestionNum(qForm.getQuestionNum());
		q.setQuestionSentence(qForm.getQuestionSentence());
		q.setQuestionAns(qForm.getQuestionAns());
		q.setQuestionExplanation(qForm.getQuestionExplanation());
		q.setPoint(q.getPoint());
		return q;
	}
	
	/** 大問エンティティに変換 */
	private BigQuestion makeBigQuestionEntity(BigQuestionCreateForm bqForm) {
		BigQuestion bq = new BigQuestion();
		bq.setBigQuestionNum(bqForm.getBigQuestionNum());
		bq.setBigQuestionSentence(bqForm.getBigQuestionSentence());
		return bq;
	}
}