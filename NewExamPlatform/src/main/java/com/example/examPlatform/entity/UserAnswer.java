package com.example.examPlatform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserAnswerテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswer {
	/** ユーザ解答ID:PK */
	@Id
	@Column(value="useransid")
	private Integer userAnsId;
	
	/** ユーザID:FK */
	@Column(value="userid")
	private Integer userId;
	
	/** レポートID:FK */
	@Column(value="reportid")
	private Integer reportId;
	
	/** 小問ID:FK */
	@Column(value="questionid")
	private Integer questionId;
	
	/** ユーザ解答選択肢ID */
	@Column(value="userans")
	private Integer userAns;
	
	/** 正誤 */
	@Column(value="tf")
	private boolean tf;
}
