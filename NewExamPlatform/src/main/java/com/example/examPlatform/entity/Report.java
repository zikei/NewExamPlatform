package com.example.examPlatform.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reportテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
	/** レポートID:PK */
	@Id
	@Column(value="reportid")
	private Integer reportId;
	
	/** ユーザID:FK */
	@Column(value="userid")
	private Integer userId;
	
	/** 試験ID:FK */
	@Column(value="examid")
	private Integer examId;
	
	/** 受験日 */
	@Column(value="examdate")
	private Date examDate;
	
	/** 得点 */
	@Column(value="score")
	private Integer score;
	
	/** 正答率 */
	@Column(value="correctanswerrate")
	private Double correctAnswerRate;
	
	/** 試験経過時間(分) */
	@Column(value="usetimeminutes")
	private Integer useTimeMinutes;
}
