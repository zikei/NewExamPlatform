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
import com.example.examPlatform.form.BigQuestionCreateForm;
import com.example.examPlatform.form.ChoicesCreateForm;
import com.example.examPlatform.form.ExamCreateForm;
import com.example.examPlatform.form.ExamQuestionCreateForm;
import com.example.examPlatform.form.QuestionCreateForm;
import com.example.examPlatform.form.TagCreateForm;
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

/* ================================================================================================================== */
	
	/** 試験概要登録ページ　*/
	@GetMapping("/Create")
	public String ExamCreateView(Model model) {
		setTagGanreToModel(model);
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
		addTag(examform);
		return ExamCreateView(model);
	}
	
	/** 試験概要登録ページタグ入力欄を削除　*/
	@PostMapping(value="/Create", params="removeTag")
	public String ExamCreateRemoveTag(@RequestParam Integer removeTag, ExamCreateForm examform, Model model) {
		removeTag(examform, removeTag);
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
		
		//試験概要情報が見つからない場合試験概要登録ページに遷移
		if(exam == null || exam.getQuestionFormat() == null) return ExamCreateView(model);
		
		//小問形式なら小問問題登録、大問形式なら大問問題登録へ遷移
		return QuestionCreateViewName(exam);
	}
	
	/** 試験問題登録ページセッションタイムを延長　*/
	@PostMapping(value="/Create/Question", params="save")
	public String QuestionCreateUpdSession(ExamQuestionCreateForm questionForm, HttpSession session, Model model) {
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ大問入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addBQ")
	public String ExamCreateAddBQ(ExamQuestionCreateForm questionForm, HttpSession session, Model model) {
		addBQ(questionForm);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ大問入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeBQ")
	public String ExamCreateRemoveBQ(@RequestParam Integer removeBQ, ExamQuestionCreateForm questionForm,
			HttpSession session, Model model) {
		removeBQ(questionForm, removeBQ);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ小問入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addQ")
	public String ExamCreateAddQ(@RequestParam Integer addQ, ExamQuestionCreateForm questionForm, HttpSession session,
			Model model) {
		addQ(questionForm, addQ);
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ小問入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeQ")
	public String ExamCreateRemoveQ(@RequestParam String removeQ, ExamQuestionCreateForm questionForm,
			HttpSession session, Model model) {
		try {
			removeQ(questionForm, removeQ);
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ選択肢入力欄を追加　*/
	@PostMapping(value="/Create/Question", params="addChoices")
	public String ExamCreateAddChoices(@RequestParam String addChoices, ExamQuestionCreateForm questionForm,
			HttpSession session, Model model) {
		try {
			addC(questionForm, addChoices);
		}catch(NumberFormatException e) {
		}
		return QuestionCreateView(session, model);
	}
	
	/** 試験問題登録ページ選択肢入力欄を削除　*/
	@PostMapping(value="/Create/Question", params="removeChoices")
	public String ExamCreateRemoveChoices(@RequestParam String removeChoices, ExamQuestionCreateForm questionForm,
			HttpSession session, Model model) {
		try {
			removeC(questionForm, removeChoices);
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
	
	/** 試験概要更新ページ　*/
	@GetMapping("/Upd/{examId}")
	public String UpdExamView(@PathVariable Integer examId, ExamCreateForm eForm, Model model) {
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
	@PostMapping(value="/Upd/{examId}", params="s")
	public String ExamUpdateUpdSession(@PathVariable Integer examId, ExamCreateForm eForm, Model model) {
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
	public String ExamUpdateAddTag(@PathVariable Integer examId, ExamCreateForm examform, Model model) {
		addTag(examform);
		return ExamUpdateUpdSession(examId, examform, model);
	}
	
	/** 試験概要更新ページタグ入力欄を削除　*/
	@PostMapping(value="/Upd/{examId}", params="removeTag")
	public String ExamUpdateRemoveTag(@PathVariable Integer examId, @RequestParam Integer removeTag,
			ExamCreateForm examform, Model model) {
		removeTag(examform, removeTag);
		return ExamUpdateUpdSession(examId, examform, model);
	}
	
	/** 試験概要更新処理 */
	@PostMapping("/Upd/{examId}")
	public String ExamUpdateRemoveTag(@Validated ExamCreateForm examform, BindingResult bindingResult, 
			@PathVariable Integer examId,  Model model) {
		if(bindingResult.hasErrors()) {
			return ExamCreateView(model);
		}
		
		Exam exam;
		try {
			exam = findCreateExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたは取得した試験がログインユーザの試験以外の場合エラーページに遷移
			return "error";
		}
		
		ExamData examData = makeExamData(examform, exam.getUserId());
		examService.examUpdate(examData);
		
		setTagGanreToModel(model);
		return "updExam";
	}
	
	/** 試験問題更新ページ　*/
	@GetMapping("/Upd/Q/{examId}")
	public String QuestionUpdateView(@PathVariable Integer examId, ExamQuestionCreateForm eqForm, Model model) {
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
	private void addTag(ExamCreateForm examform) {
		examform.addTag();
	}
	
	/** フォームの指定されたタグを削除 */
	private void removeTag(ExamCreateForm examform, Integer index) {
		examform.removeTag(index);
	}
	
	/** フォームに新規大問を追加 */
	private void addBQ(ExamQuestionCreateForm questionForm) {
		questionForm.addBQ();
	}
	
	/** フォームの指定された大門を削除 */
	private void removeBQ(ExamQuestionCreateForm questionForm, Integer index) {
		questionForm.removeBQ(index);
	}
	
	/** フォームに新規小問を追加 */
	private void addQ(ExamQuestionCreateForm questionForm, Integer index) {
		questionForm.addQ(index);
	}
	
	/** フォームの指定された小問を削除 */
	private void removeQ(ExamQuestionCreateForm questionForm, String removeQ) throws NumberFormatException{
		Integer[] tmp = makeIntegerArray(removeQ);
		if(tmp.length == 2) {
			Integer bqidx = tmp[0];
			Integer qidx  = tmp[1];
			questionForm.removeQ(bqidx, qidx);
		}
	}
	
	/** フォームの指定された小問に選択肢を追加 */
	private void addC(ExamQuestionCreateForm questionForm, String addC) throws NumberFormatException{
		Integer[] tmp = makeIntegerArray(addC);
		if(tmp.length == 2) {
			Integer bqidx = tmp[0];
			Integer qidx  = tmp[1];
			questionForm.addChoices(bqidx, qidx);
		}
	}
	
	/** フォームの指定された選択肢を削除 */
	private void removeC(ExamQuestionCreateForm questionForm, String removeC) throws NumberFormatException{
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
	private ExamCreateForm makeExamCreateForm(Exam exam) {
		List<String> tagList = examService.selectTagByExamId(exam.getExamId());
		List<TagCreateForm> tagFormList = new ArrayList<>();
		tagList.forEach(s -> tagFormList.add(new TagCreateForm(s)));
		
		ExamCreateForm eForm = new ExamCreateForm();
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
	
	/** ExamQuestonをExamQuestionCreateFormに変換 */
	private ExamQuestionCreateForm makeExamQuestionForm(ExamQuestion eq) {
		List<BigQuestionData> bqdList = eq.getBigQuestionList();
		List<BigQuestionCreateForm> bqfList = makeBigQuestionForm(bqdList);
		return new ExamQuestionCreateForm(bqfList);
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

	/** BigQuestionDataListをBigQuestionCreateFormListに変換 */
	private List<BigQuestionCreateForm> makeBigQuestionForm(List<BigQuestionData> bqdList) {
		List<BigQuestionCreateForm> bqfList = new ArrayList<>();
		for(BigQuestionData bqd : bqdList) {
			List<QuestionData> qdList = bqd.getQuestionList();
			BigQuestion bq =bqd.getBigQuestion();
			
			List<QuestionCreateForm> qfList = makeQuestionForm(qdList);
			bqfList.add(new BigQuestionCreateForm(bq, qfList));
		}
		return bqfList;
	}
	
	/** QuestionDataListをQuestionCreateFormListに変換 */
	private List<QuestionCreateForm> makeQuestionForm(List<QuestionData> qdList) {
		List<QuestionCreateForm> qfList = new ArrayList<>();
		for(QuestionData qd : qdList) {
			List<Choices> cList = qd.getChoicesList();
			Question q =qd.getQuestion();
			
			List<ChoicesCreateForm> cfList = makeChoicesForm(cList);
			qfList.add(new QuestionCreateForm(q, cfList));
		}
		return qfList;
	}
	
	/** ChoicesListをChoicesCreateFormListに変換 */
	private List<ChoicesCreateForm> makeChoicesForm(List<Choices> cList) {
		List<ChoicesCreateForm> cfList = new ArrayList<>();
		for(Choices c : cList) {
			cfList.add(new ChoicesCreateForm(c));
		}
		return cfList;
	}
}