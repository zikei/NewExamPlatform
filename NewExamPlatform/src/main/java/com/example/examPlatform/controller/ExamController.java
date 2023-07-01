package com.example.examPlatform.controller;

import java.util.ArrayList;
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
import com.example.examPlatform.exception.ResourceAccessException;
import com.example.examPlatform.form.BigQuestionForm;
import com.example.examPlatform.form.ChoicesForm;
import com.example.examPlatform.form.ExamForm;
import com.example.examPlatform.form.ExamQuestionForm;
import com.example.examPlatform.form.QuestionCreateForm;
import com.example.examPlatform.form.TagForm;
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
	@GetMapping("/Create")
	public String ExamCreateView(Model model) {
		setTagGanreToModel(model);
		return "examCreate";
	}
	
	/** 試験概要登録ページセッションタイムを延長　*/
	@PostMapping(value="/Create", params="save")
	public String ExamCreateUpdSession(ExamForm examform, Model model) {
		return ExamCreateView(model);
	}
	
	/** 試験概要登録ページタグ入力欄を追加　*/
	@PostMapping(value="/Create", params="addTag")
	public String ExamCreateAddTag(ExamForm examform, Model model) {
		addTag(examform);
		return ExamCreateView(model);
	}
	
	/** 試験概要登録ページタグ入力欄を削除　*/
	@PostMapping(value="/Create", params="removeTag")
	public String ExamCreateRemoveTag(@RequestParam Integer removeTag, ExamForm examform, Model model) {
		removeTag(examform, removeTag);
		return ExamCreateView(model);
	}
	
	/** 試験概要処理　*/
	@PostMapping("/Create")
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
		
		ExamData exam = makeExamData(examform, loginUser.getUserId());
		session.setAttribute("exam", exam);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ　*/
	@GetMapping("/Create/Question")
	public String QuestionCreateView(HttpSession session, Model model) {
		ExamData examData = (ExamData) session.getAttribute("exam");
		Exam exam = examData.getExam();
		
		//試験概要情報が見つからない場合試験概要登録ページに遷移
		if(exam == null || exam.getQuestionFormat() == null) return ExamCreateView(model);
		
		//小問形式なら小問問題登録、大問形式なら大問問題登録へ遷移
		return QuestionCreateViewName(exam);
	}
	
	/** 試験問題登録ページセッションタイムを延長　*/
	@PostMapping(value="/Create/Question", params="save")
	public String QuestionCreateUpdSession(ExamQuestionForm questionForm, HttpSession session, Model model) {
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ大問入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addBQ")
	public String ExamCreateAddBQ(ExamQuestionForm questionForm, HttpSession session, Model model) {
		addBQ(questionForm);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ大問入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeBQ")
	public String ExamCreateRemoveBQ(@RequestParam Integer removeBQ, ExamQuestionForm questionForm,
			HttpSession session, Model model) {
		removeBQ(questionForm, removeBQ);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ小問入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addQ")
	public String ExamCreateAddQ(@RequestParam Integer addQ, ExamQuestionForm questionForm, HttpSession session,
			Model model) {
		addQ(questionForm, addQ);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ小問入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeQ")
	public String ExamCreateRemoveQ(@RequestParam String removeQ, ExamQuestionForm questionForm,
			HttpSession session, Model model) {
		try {
			removeQ(questionForm, removeQ);
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ選択肢入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addChoices")
	public String ExamCreateAddChoices(@RequestParam String addChoices, ExamQuestionForm questionForm,
			HttpSession session, Model model) {
		try {
			addC(questionForm, addChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ選択肢入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeChoices")
	public String ExamCreateRemoveChoices(@RequestParam String removeChoices, ExamQuestionForm questionForm,
			HttpSession session, Model model) {
		try {
			removeC(questionForm, removeChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題処理　*/
	@PostMapping("/Create/Question")
	public String QuestionCreate(@Validated ExamQuestionForm questionForm, BindingResult bindingResult,
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
		eForm = makeExamCreateForm(exam);
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
		addTag(examform);
		return ExamUpdateUpdSession(examId, examform, model);
	}
	
	/** 試験概要更新ページタグ入力欄を削除　*/
	@PostMapping(value="/Upd/{examId}", params="removeTag")
	public String ExamUpdateRemoveTag(@PathVariable Integer examId, @RequestParam Integer removeTag,
			ExamForm examform, Model model) {
		removeTag(examform, removeTag);
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
		
		ExamData examData = makeExamData(examform, exam.getUserId());
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
		eqForm = makeExamQuestionForm(eq);
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
		addBQ(questionForm);
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ大問入力欄を削除　*/
	@PostMapping(value="/Upd/Q/{examId}", params="removeBQ")
	public String QuestionUpdateRemoveBQ(@PathVariable Integer examId, @RequestParam Integer removeBQ, 
			ExamQuestionForm questionForm, Model model) {
		removeBQ(questionForm, removeBQ);
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ小問入力欄を追加　*/
	@PostMapping(value="/Upd/Q/{examId}", params="addQ")
	public String QuestionUpdateAddQ(@PathVariable Integer examId, @RequestParam Integer addQ,
			ExamQuestionForm questionForm, Model model) {
		addQ(questionForm, addQ);
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ小問入力欄を削除　*/
	@PostMapping(value="/Upd/Q/{examId}", params="removeQ")
	public String QuestionUpdateRemoveQ(@PathVariable Integer examId, @RequestParam String removeQ,
			ExamQuestionForm questionForm, Model model) {
		try {
			removeQ(questionForm, removeQ);
		}catch(NumberFormatException e) {
		}
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ選択肢入力欄を追加　*/
	@PostMapping(value="/Upd/Q/{examId}", params="addChoices")
	public String QuestionUpdateAddChoices(@PathVariable Integer examId, @RequestParam String addChoices,
			ExamQuestionForm questionForm, Model model) {
		try {
			addC(questionForm, addChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新ページ選択肢入力欄を削除　*/
	@PostMapping(value="/Upd/Q/{examId}", params="removeChoices")
	public String QuestionUpdateRemoveChoices(@PathVariable Integer examId, @RequestParam String removeChoices,
			ExamQuestionForm questionForm, Model model) {
		try {
			removeC(questionForm, removeChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionUpdateUpdSession(examId, questionForm, model);
	}
	
	/** 試験問題更新処理 */
	@PostMapping("/Upd/{examId}")
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
		
		ExamQuestion examQuestion = makeExamQuestion(questionForm);
		
		examService.examQuestionUpdate(examQuestion);
		
		model.addAttribute("msg", "更新が完了しました");
		//小問形式なら小問問題更新、大問形式なら大問問題更新へ遷移
		return QuestionUpdateViewName(exam);
	}
	
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
	
	/** 試験問題更新ページのビュー名を返す */
	private String QuestionCreateViewName(Exam exam) {
		QuestionFormat qf = new QuestionFormat();
		//小問形式なら小問問題登録、大問形式なら大問問題登録へ遷移
		if(qf.isBigQuestionFormat(exam.getDisclosureRange())) {
			return "bigQuestionCreate";
		}else {
			return "questionCreate";
		}
	}
	
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
	
	/** フォームに新規タグを追加 */
	private void addTag(ExamForm examform) {
		examform.addTag();
	}
	
	/** フォームの指定されたタグを削除 */
	private void removeTag(ExamForm examform, Integer index) {
		examform.removeTag(index);
	}
	
	/** フォームに新規大問を追加 */
	private void addBQ(ExamQuestionForm questionForm) {
		questionForm.addBQ();
	}
	
	/** フォームの指定された大門を削除 */
	private void removeBQ(ExamQuestionForm questionForm, Integer index) {
		questionForm.removeBQ(index);
	}
	
	/** フォームに新規小問を追加 */
	private void addQ(ExamQuestionForm questionForm, Integer index) {
		questionForm.addQ(index);
	}
	
	/** フォームの指定された小問を削除 */
	private void removeQ(ExamQuestionForm questionForm, String removeQ) throws NumberFormatException{
		Integer[] tmp = makeIntegerArray(removeQ);
		if(tmp.length == 2) {
			Integer bqidx = tmp[0];
			Integer qidx  = tmp[1];
			questionForm.removeQ(bqidx, qidx);
		}
	}
	
	/** フォームの指定された小問に選択肢を追加 */
	private void addC(ExamQuestionForm questionForm, String addC) throws NumberFormatException{
		Integer[] tmp = makeIntegerArray(addC);
		if(tmp.length == 2) {
			Integer bqidx = tmp[0];
			Integer qidx  = tmp[1];
			questionForm.addChoices(bqidx, qidx);
		}
	}
	
	/** フォームの指定された選択肢を削除 */
	private void removeC(ExamQuestionForm questionForm, String removeC) throws NumberFormatException{
		Integer[] tmp = makeIntegerArray(removeC);
		if(tmp.length == 3) {
			Integer bqidx = tmp[0];
			Integer qidx  = tmp[1];
			Integer cidx  = tmp[2];
			questionForm.removeChoices(bqidx, qidx, cidx);
		}
	}
	
	/** タグリストとジャンルリストをModelにセット */
	private void setTagGanreToModel(Model model) {
		List<String> tagList = examService.selectTag();
		List<Ganre> ganreList = examService.selectAllGanre();
		model.addAttribute("tagList", tagList);
		model.addAttribute("ganreList", ganreList);
	}
	
	/** Examエンティティをformに変換 */
	private ExamForm makeExamCreateForm(Exam exam) {
		List<String> tagList = examService.selectTagByExamId(exam.getExamId());
		List<TagForm> tagFormList = new ArrayList<>();
		tagList.forEach(s -> tagFormList.add(new TagForm(s)));
		
		ExamForm eForm = new ExamForm();
		eForm.setGenreId(exam.getGenreId());
		eForm.setExamName(exam.getExamName());
		eForm.setPassingScore(exam.getPassingScore());
		eForm.setExamTimeMinutes(exam.getExamTimeMinutes());
		eForm.setExamExplanation(exam.getExamExplanation());
		eForm.setDisclosureRange(exam.getDisclosureRange());
		eForm.setLimitedPassword(exam.getLimitedPassword());
		eForm.setQuestionFormat(exam.getQuestionFormat());
		eForm.setTagList(tagFormList);
		
		return eForm;
	}
	
	/** ExamQuestonをExamQuestionFormに変換 */
	private ExamQuestionForm makeExamQuestionForm(ExamQuestion eq) {
		List<BigQuestionData> bqdList = eq.getBigQuestionList();
		List<BigQuestionForm> bqfList = makeBigQuestionForm(bqdList);
		return new ExamQuestionForm(bqfList);
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
	private ExamData makeExamData(ExamForm eForm, Integer userId) {
		Exam exam = makeExam(eForm, userId);
		List<String> tagList = new ArrayList<>();
		eForm.getTagList().forEach(t -> tagList.add(t.getTag()));
		tagList.removeIf(s -> s==null || s.isBlank());
		
		ExamData examData = new ExamData(exam, tagList);
		return examData;
	}
	
	/** formをExam形式に変換 */
	private Exam makeExam(ExamForm eForm, Integer userId) {
		
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
		return exam;
	}
	
	/** ExamQuestionに変換 */
	private ExamQuestion makeExamQuestion(ExamQuestionForm form){
		List<BigQuestionForm> bqFormList = form.getBigQuestionCreateForm();
		List<BigQuestionData> bqDataList = new ArrayList<BigQuestionData>();
		
		for(BigQuestionForm bqForm : bqFormList) {
			bqDataList.add(makeBigQuestionData(bqForm));
		}
		return new ExamQuestion(bqDataList);
	}
	
	/** BigQuestionDataに変換 */
	private BigQuestionData makeBigQuestionData(BigQuestionForm form){
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
		
		List<ChoicesForm> cFormList = form.getChoicesFormList();
		List<Choices> cList = new ArrayList<Choices>();
		
		for(ChoicesForm cForm : cFormList) {
			cList.add(makeChoicesEntity(cForm));
		}
		
		return new QuestionData(qEntity, cList);
	}
	
	/** 選択肢エンティティに変換 */
 	private Choices makeChoicesEntity(ChoicesForm cForm) {
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
	private BigQuestion makeBigQuestionEntity(BigQuestionForm bqForm) {
		BigQuestion bq = new BigQuestion();
		bq.setBigQuestionNum(bqForm.getBigQuestionNum());
		bq.setBigQuestionSentence(bqForm.getBigQuestionSentence());
		return bq;
	}

	/** BigQuestionDataListをBigQuestionFormListに変換 */
	private List<BigQuestionForm> makeBigQuestionForm(List<BigQuestionData> bqdList) {
		List<BigQuestionForm> bqfList = new ArrayList<>();
		for(BigQuestionData bqd : bqdList) {
			List<QuestionData> qdList = bqd.getQuestionList();
			BigQuestion bq =bqd.getBigQuestion();
			
			List<QuestionCreateForm> qfList = makeQuestionForm(qdList);
			bqfList.add(new BigQuestionForm(bq, qfList));
		}
		return bqfList;
	}
	
	/** QuestionDataListをQuestionFormListに変換 */
	private List<QuestionCreateForm> makeQuestionForm(List<QuestionData> qdList) {
		List<QuestionCreateForm> qfList = new ArrayList<>();
		for(QuestionData qd : qdList) {
			List<Choices> cList = qd.getChoicesList();
			Question q =qd.getQuestion();
			
			List<ChoicesForm> cfList = makeChoicesForm(cList);
			qfList.add(new QuestionCreateForm(q, cfList));
		}
		return qfList;
	}
	
	/** ChoicesListをChoicesFormListに変換 */
	private List<ChoicesForm> makeChoicesForm(List<Choices> cList) {
		List<ChoicesForm> cfList = new ArrayList<>();
		for(Choices c : cList) {
			cfList.add(new ChoicesForm(c));
		}
		return cfList;
	}
}