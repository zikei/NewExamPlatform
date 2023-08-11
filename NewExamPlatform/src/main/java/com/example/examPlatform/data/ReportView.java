package com.example.examPlatform.data;

import java.util.Date;

import com.example.examPlatform.entity.Report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * レポート表示用クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportView {
	/** レポートID*/
	private Integer reportId;
	
	/** ユーザ名 */
	private String userName;
	
	/** 試験ID */
	private Integer examId;
	
	/** 試験名 */
	private String examName;
	
	/** 受験日 */
	private Date examDate;
	
	/** 得点 */
	private Integer score;
	
	/** 試験経過時間(分) */
	private Integer useTimeMinutes;
	
	/** やり直し試験 */
	private boolean isRedo;
	
	/** 合否 */
	private boolean isSuccess;
	
	/** 詳細ページ有無 */
	private boolean isDetail;
	
	
	public ReportView(Report repo, String examName, String userName, boolean isDetail) {
		this.reportId       = repo.getReportId();
		this.userName       = userName;
		this.examId         = repo.getExamId();
		this.examName       = examName;
		this.examDate       = repo.getExamDate();
		this.score          = repo.getScore();
		this.useTimeMinutes = repo.getUseTimeMinutes();
		this.isRedo         = repo.isRedo();
		this.isSuccess      = repo.isSuccess();
		this.isDetail       = isDetail;
	}
	
}
