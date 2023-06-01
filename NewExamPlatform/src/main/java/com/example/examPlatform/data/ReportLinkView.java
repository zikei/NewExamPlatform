package com.example.examPlatform.data;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Report;
import com.example.examPlatform.service.ExamService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** レポートリンク表示用試験クラス */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportLinkView {
	@Autowired
	ExamService examService;
	
	/** レポートID */
	private Integer examId;
	
	/** 試験名 */
	private String examName;
	
	/** 試験名 */
	private Date examDate;
	
	public ReportLinkView(Report report) {
		Exam exam = examService.selectExamByExamId(report.getExamId()).get();
		
		this.examId = report.getExamId();
		this.examName = exam.getExamName();
		this.examDate = report.getExamDate();
	}
}