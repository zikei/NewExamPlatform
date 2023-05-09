package com.example.examPlatform.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Examテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
	/** 試験ID */
	@Id
	@Column(value="examid")
	private Integer examId;
	
	/** ユーザID:FK */
	@Column(value="userid")
	private Integer userId;
	
	/** ジャンルID:FK */
	@Column(value="genreid")
	private Integer genreId;
	
	/** 試験名 */
	@Column(value="examname")
	private String examName;
	
	/** 試験作成日 */
	@Column(value="createdate")
	private Date createDate;
	
	/** 試験更新日 */
	@Column(value="updatedate")
	private Date updateDate;
	
	/** 合格点 */
	@Column(value="passingscore")
	private Integer passingScore;
	
	/** 試験時間(分) */
	@Column(value="examtimeminutes")
	private Integer examTimeMinutes;
	
	/** 試験概要 */
	@Column(value="examexplanation")
	private String examExplanation;
	
	/** 公開範囲 */
	@Column(value="disclosurerange")
	private Integer disclosureRange;
	
	/** 限定公開パスワード */
	@Column(value="limitedpassword")
	private String limitedPassword;
	
	/** 問題形式 */
	@Column(value="questionformat")
	private Integer questionFormat;
}