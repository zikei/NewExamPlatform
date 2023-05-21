package com.example.examPlatform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BigQuestionテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BigQuestion {
	/** 大問ID:PK */
	@Column(value="bigquestionid")
	@Id
	private Integer bigQuestionId;
	
	/** 試験ID:FK */
	@Column(value="examid")
	private Integer examId;
	
	/** 大問番号 */
	@Column(value="bigquestionnum")
	private Integer BigQuestionNum;
	
	/** 問題文 */
	@Column(value="bigquestionsentence")
	private String BigQuestionSentence;
}