package com.example.examPlatform.data.question;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 試験問題クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestion {
	/** 試験Id */
	private Integer examId;
	
	/** 大問リスト */
	private List<BigQuestionData> bigQuestionList;
	
	public ExamQuestion(List<BigQuestionData> bigQuestionList) {
		this.examId = null;
		this.bigQuestionList = bigQuestionList;
	}
	
	public int fullScore(){
		int score = 0;
		for(BigQuestionData bqData: bigQuestionList) {
			for(QuestionData qData: bqData.getQuestionList()) {
				score += qData.getQuestion().getPoint();
			}
		}
		return score;
	}
}
