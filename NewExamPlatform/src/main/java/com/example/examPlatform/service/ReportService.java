package com.example.examPlatform.service;

import java.util.Date;

import com.example.examPlatform.data.ActInfo;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Report;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.exception.ScoringException;
import com.example.examPlatform.form.ExamActForm;

/** 試験関連処理 */
public interface ReportService {
	/** 採点を行いレポートを保存する 
	 *  @throws ScoringException */
	Report makeReport(ExamActForm actForm, ActInfo actInfo, Date endTime, Exam exam) throws ScoringException;
	
	/** レポートIDでレポートを検索し取得
	 *  @throws NotFoundException */
	Report selectReport(Integer repoId) throws NotFoundException;
}