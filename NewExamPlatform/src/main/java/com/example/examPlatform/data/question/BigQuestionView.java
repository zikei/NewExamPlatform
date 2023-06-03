package com.example.examPlatform.data.question;

import java.util.List;

import com.example.examPlatform.entity.BigQuestion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 大問表示用クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BigQuestionView {
	/** 大問ID */
	private Integer bigQuestionId;
	
	/** 大問番号 */
	private Integer BigQuestionNum;
	
	/** 問題文 */
	private String bigQuestionSentence;
	
	private List<QuestionView> quesitonViewList;
	
	public BigQuestionView(BigQuestion bq, List<QuestionView> qList) {
		this.bigQuestionId       = bq.getBigQuestionId();
		this.BigQuestionNum      = bq.getBigQuestionNum();
		this.bigQuestionSentence = bq.getBigQuestionSentence();
		this.quesitonViewList    = qList;
	}
}
