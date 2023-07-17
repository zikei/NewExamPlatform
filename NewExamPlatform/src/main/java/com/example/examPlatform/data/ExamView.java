package com.example.examPlatform.data;

import java.util.Date;
import java.util.List;

import com.example.examPlatform.entity.Exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 試験表示用クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamView {
	/** 試験ID */
	private Integer examId;
	
	/** ユーザ名 */
	private String userName;
	
	/** ジャンル */
	private String genre;
	
	/** タグ */
	private List<String> tagList;
	
	/** 試験名 */
	private String examName;
	
	/** 試験作成日 */
	private Date createDate;
	
	/** 試験更新日 */
	private Date updateDate;
	
	/** 合格点 */
	private Integer passingScore;
	
	/** 試験時間(分) */
	private Integer examTimeMinutes;
	
	/** 試験概要 */
	private String examExplanation;
	
	public ExamView(Exam exam, String userName, String genre, List<String> tagList) {
		this.examId          = exam.getExamId();
		this.userName        = userName;
		this.genre           = genre;
		this.tagList         = tagList;
		this.examName        = exam.getExamName();
		this.createDate      = exam.getCreateDate();
		this.updateDate      = exam.getUpdateDate();
		this.passingScore    = exam.getPassingScore();
		this.examTimeMinutes = exam.getExamTimeMinutes();
		this.examExplanation = exam.getExamExplanation();
	}
}
