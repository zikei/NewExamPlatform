package com.example.examPlatform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Questionテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
	/** 小問ID:PK */
	@Column(value="questionid")
	@Id
	private Integer questionId;
	
	/** 大問ID:FK */
	@Column(value="bigquestionid")
	private Integer bigQuestionId;
	
	/** 小問番号 */
	@Column(value="questionnum")
	private Integer questionNum;
	
	/** 問題文 */
	@Column(value="questionsentence")
	private String questionSentence;
	
	/** 正答選択肢ID:FK */
	@Column(value="questionanswer")
	private Integer questionAns;
	
	/** 解説 */
	@Column(value="questionexplanation")
	private String questionExplanation;
	
	/** 配点 */
	@Column(value="allocationofpoint")
	private Integer point;
}