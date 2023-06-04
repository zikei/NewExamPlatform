package com.example.examPlatform.service;

import java.util.List;
import java.util.Optional;

import com.example.examPlatform.data.link.ExamLinkView;
import com.example.examPlatform.data.question.BigQuestionData;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.form.ExamCreateForm;

/** 試験関連処理 */
public interface ExamService {
	/** 試験全件取得 */
	List<Exam> selectAllExam();
	
	/** 試験取得 */
	Optional<Exam> selectExamByExamId(Integer examId);
	
	/** ユーザIDで試験取得 */
	List<Exam> selectExamByUserID(Integer userId);
	
	/** 試験登録 */
	void examRegister(Exam exam, List<BigQuestionData> questionData);
	
	/** formをExam形式に変換 */
	Exam makeExam(ExamCreateForm form, Integer userId);
	
	/** 試験リストを試験リンクリスト形式に変換*/
	List<ExamLinkView> makeExamLinkList(List<Exam> examList);
}