package com.example.examPlatform.service;

import com.example.examPlatform.data.ExamData;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.form.ExamForm;
import com.example.examPlatform.form.ExamQuestionForm;

/** 試験関連フォーム処理 */
public interface ExamFormCtrService {
	/** フォームに新規タグを追加 */
	void addTag(ExamForm examform);
	
	/** フォームの指定されたタグを削除 */
	void removeTag(ExamForm examform, Integer index);
	
	/** フォームに新規大問を追加 */
	void addBQ(ExamQuestionForm questionForm);
	
	/** フォームの指定された大門を削除 */
	void removeBQ(ExamQuestionForm questionForm, Integer index);
	
	/** フォームに新規小問を追加 */
	void addQ(ExamQuestionForm questionForm, Integer index);
	
	/** フォームの指定された小問を削除 */
	void removeQ(ExamQuestionForm questionForm, String removeQ) throws NumberFormatException;
	
	/** フォームの指定された小問に選択肢を追加 */
	void addC(ExamQuestionForm questionForm, String addC) throws NumberFormatException;
	
	/** フォームの指定された選択肢を削除 */
	void removeC(ExamQuestionForm questionForm, String removeC) throws NumberFormatException;
	
	/** Examエンティティをformに変換 */
	ExamForm makeExamCreateForm(Exam exam);
	
	/** formをExamData形式に変換 */
	ExamData makeExamData(ExamForm eForm, Integer userId);
	
	/** ExamQuestonをExamQuestionFormに変換 */
	ExamQuestionForm makeExamQuestionForm(ExamQuestion eq);
	
	/** ExamQuestionに変換 */
	ExamQuestion makeExamQuestion(ExamQuestionForm form);
}