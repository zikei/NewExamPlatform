package com.example.examPlatform.data.question;

import java.util.List;

import com.example.examPlatform.entity.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小問表示用クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionView {
	/** 小問ID */
	private Integer questionId;
	
	/** 小問番号 */
	private Integer questionNum;
	
	/** 問題文 */
	private String questionSentence;
	
	/** 選択肢リスト */
	private List<ChoicesView> choicesViewList;
	
	public QuestionView(Question q, List<ChoicesView> cList) {
		this.questionId       = q.getQuestionId();
		this.questionNum      = q.getQuestionNum();
		this.questionSentence = q.getQuestionSentence();
		this.choicesViewList  = cList;
	}
}
