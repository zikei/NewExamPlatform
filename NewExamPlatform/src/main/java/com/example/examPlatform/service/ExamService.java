package com.example.examPlatform.service;

import java.util.List;
import java.util.Optional;

import com.example.examPlatform.data.ExamData;
import com.example.examPlatform.data.link.ExamLinkView;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Ganre;

/** 試験関連処理 */
public interface ExamService {
	/** 試験全件取得 */
	List<Exam> selectAllExam();
	
	/** 試験取得 */
	Optional<Exam> selectExamByExamId(Integer examId);
	
	/** ユーザIDで試験取得 */
	List<Exam> selectExamByUserID(Integer userId);
	
	/** タグ重複なし全件取得 */
	List<String> selectTag();
	
	List<String> selectTagByExamId(Integer examId);
	
	/** ジャンル全件取得 */
	List<Ganre> selectAllGanre();
	
	/** 試験登録 */
	void examRegister(ExamData examData, ExamQuestion examQuestion);
	
	/** 試験リストを試験リンクリスト形式に変換*/
	List<ExamLinkView> makeExamLinkList(List<Exam> examList);
}