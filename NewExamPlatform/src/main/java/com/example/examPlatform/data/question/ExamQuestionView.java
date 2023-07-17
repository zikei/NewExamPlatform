package com.example.examPlatform.data.question;

import java.util.List;

import com.example.examPlatform.entity.Exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 試験問題表示用クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionView {
	/** 試験Id */
	private Integer examId;
	
	/** 試験名 */
	private String examName;
	
	/** 試験時間(分) */
	private Integer examTimeMinutes;
	
	/** 大問リスト */
	private List<BigQuestionView> bigQuestionList;
	
	public ExamQuestionView(Exam exam, List<BigQuestionView> bigQuestionList) {
		this.examId          = exam.getExamId();
		this.examName        = exam.getExamName();
		this.examTimeMinutes = exam.getExamTimeMinutes();
		this.bigQuestionList = bigQuestionList;
	}
}
