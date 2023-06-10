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
	/** 大問リスト */
	private List<BigQuestionData> bigQuestionList;
	
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
