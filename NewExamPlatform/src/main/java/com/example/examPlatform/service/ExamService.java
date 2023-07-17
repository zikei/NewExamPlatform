package com.example.examPlatform.service;

import java.util.List;
import java.util.Optional;

import com.example.examPlatform.data.ExamData;
import com.example.examPlatform.data.link.ExamLinkView;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Ganre;
import com.example.examPlatform.exception.NotFoundException;

/** 試験関連処理 */
public interface ExamService {
	/** 試験全件取得 */
	List<Exam> selectAllExam();
	
	/** 試験取得 */
	Optional<Exam> selectExamByExamId(Integer examId);
	
	/** 試験問題取得 */
	ExamQuestion selectExamQuestionByExamId(Integer examId);
	
	/** ユーザIDで試験取得 */
	List<Exam> selectExamByUserID(Integer userId);
	
	/** 指定された試験のタグを取得 */
	List<String> selectTagByExamId(Integer examId);
	
	/** タグ重複なし全件取得 */
	List<String> selectTag();
	
	/** ジャンル全件取得 */
	List<Ganre> selectAllGanre();
	
	/** 指定された試験のジャンルを取得 */
	Ganre selectGanreByGanreId(Integer ganreId) throws NotFoundException;
	
	/** 試験登録 */
	void examRegister(ExamData examData, ExamQuestion examQuestion);
	
	/** 試験概要更新 */
	void examUpdate(ExamData examData);
	
	/** 試験問題更新 */
	void examQuestionUpdate(ExamQuestion examQuestion);
	
	/** 試験削除 */
	void examDelete(Integer examId);
	
	/** 試験リストを試験リンクリスト形式に変換*/
	List<ExamLinkView> makeExamLinkList(List<Exam> examList);
}