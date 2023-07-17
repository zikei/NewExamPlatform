package com.example.examPlatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.examPlatform.data.AuthorizedExam;
import com.example.examPlatform.data.ExamView;
import com.example.examPlatform.data.constant.DisclosureRange;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.exception.ExamLimitedException;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.exception.ResourceAccessException;
import com.example.examPlatform.service.AccountService;
import com.example.examPlatform.service.ExamFormCtrService;
import com.example.examPlatform.service.ExamService;

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
	AuthorizedExam authorizedExam;

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
	
	/** 試験概要画面表示 */
	@GetMapping("/{examId}")
	public String ExamOverview(@PathVariable Integer examId, Model model) {
		Exam exam;
		try {
			exam = findExamById(examId, model);
		} catch (ResourceAccessException e) {
			// 試験が見つからないまたはログインユーザ以外が非公開試験にアクセスした場合エラーページに遷移
			return "error";
		} catch (ExamLimitedException e) {
			// ログインユーザ以外が認可されていない限定公開試験にアクセスした場合認証画面に遷移
			return "examLimited";
		}
		
		ExamView examView = makeExamView(exam);
		if(examView == null) {
			model.addAttribute("errorMsg", "試験概要を取得できませんでした");
			return "error";
		}
		model.addAttribute("exam", examView);
		return "exam";
	}
	
	/** 限定公開試験認証処理 */
	@PostMapping("/{examId}/Limited")
	public String ExamAuthentication(@PathVariable Integer examId, @RequestParam String pass, Model model) {
		DisclosureRange dr = new DisclosureRange();
		Exam exam;
		try {
			exam = findLimitedExamById(examId, model);
		} catch (ResourceAccessException e) {
			return "error";
		}
		
		// 試験が取得できている場合限定公開以外はアクセス可能なため概要を表示する
		// 試験がログインユーザ本人が作成した場合も概要を表示する
		if(!dr.isLimited(exam.getDisclosureRange()))     return "redirect:/ExamPlatform/"+examId;
		if(accountService.isLoginUser(exam.getUserId())) return "redirect:/ExamPlatform/"+examId;
		
		if(exam.getLimitedPassword().equals(pass)) {
			authorizedExam.add(examId);
		}else {
			// パスワードが一致しない場合
			return "examLimited";
		}
		return "redirect:/ExamPlatform/"+examId;
	}
	
/* ================================================================================================================== */
	
	/**
	 * 試験を取得する 取得できない場合メッセージを格納し例外をスロー
	 * @throws ResourceAccessException　試験が見つからないまたはログインユーザ以外が非公開試験にアクセスした場合
	 * @throws ExamLimitedException　　　ログインユーザ以外が認可されていない限定公開試験にアクセスした場合
	 */
	private Exam findExamById(Integer examId, Model model) throws ResourceAccessException, ExamLimitedException {
		DisclosureRange dr = new DisclosureRange();
		Exam exam;
		try {
			exam = examService.selectExamByExamId(examId).orElseThrow(() -> new NotFoundException("Exam NotFound"));
		} catch (NotFoundException e) {
			// 試験が見つからない場合
			model.addAttribute("errorMsg", "試験が見つかりません");
			throw new ResourceAccessException("Exam NotFound");
		}
		if(!accountService.isLoginUser(exam.getUserId()) && !dr.isOpen(exam.getDisclosureRange())) {
			// ログインユーザ以外のアクセスかつ試験が全体公開以外の場合
			if(dr.isClose(exam.getDisclosureRange())) {
				// 試験が非公開の場合
				model.addAttribute("errorMsg", "このページは表示できません");
				throw new ResourceAccessException("Exam forbidden");
			}
			if(dr.isLimited(exam.getDisclosureRange()) && !authorizedExam.isAuthorized(examId)) {
				// 試験が認可されていない限定公開試験の場合
				throw new ExamLimitedException("Exam Limited");
			}
		}
		return exam;
	}
	
	/** アクセス可能な試験を取得する 試験が見つからないまたはログインユーザ以外が非公開試験にアクセスした場合modelにメッセージを格納し例外をスローする*/
	private Exam findLimitedExamById(Integer examId, Model model) throws ResourceAccessException{
		DisclosureRange dr = new DisclosureRange();
		Exam exam;
		try {
			exam = examService.selectExamByExamId(examId).orElseThrow(() -> new NotFoundException("Exam NotFound"));
		} catch (NotFoundException e) {
			// 試験が見つからない場合
			model.addAttribute("errorMsg", "試験が見つかりません");
			throw new ResourceAccessException("Exam NotFound");
		}
		if(!accountService.isLoginUser(exam.getUserId()) && !dr.isClose(exam.getDisclosureRange())) {
			// ログインユーザ以外のアクセスかつ試験が非公開の場合
			model.addAttribute("errorMsg", "このページは表示できません");
			throw new ResourceAccessException("Exam forbidden");
		}
		return exam;
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
	
	/** ExamViewを作成する */
	private ExamView makeExamView(Exam exam) {
		String userName;
		String ganreName;
		try {
			userName  = accountService.selectAccountByUserId(exam.getUserId()).getUserName();
			ganreName = examService.selectGanreByGanreId(exam.getGenreId()).getGanreName();
		} catch (NotFoundException e) {
			return null;
		}
		List<String> tagList = examService.selectTagByExamId(exam.getExamId());
		
		return new ExamView(exam, userName, ganreName, tagList);
	}
}