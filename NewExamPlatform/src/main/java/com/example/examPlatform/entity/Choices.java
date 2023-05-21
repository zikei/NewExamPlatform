package com.example.examPlatform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Choicesテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Choices {
	/** 選択肢ID:PK */
	@Column(value="choicesid")
	@Id
	private Integer choicesId;
	
	/** 小問ID:FK */
	@Column(value="questionid")
	private Integer questionId;
	
	/** 選択肢番号 */
	@Column(value="choicesnum")
	private Integer choicesNum;
	
	/** 選択肢 */
	@Column(value="choices")
	private String choices;
}