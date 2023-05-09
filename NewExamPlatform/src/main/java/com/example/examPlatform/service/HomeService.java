package com.example.examPlatform.service;

import java.util.List;

import com.example.examPlatform.entity.Exam;

/** ホーム関連処理 */
public interface HomeService {
	/** 新着順試験取得 */
	List<Exam> selectNewArrivalsExams();
	
	/** 月間受験上位試験取得 */
	List<Exam> selectMonthlyExeTopExams();
	
	/** ブックマーク数順試験取得 */
	List<Exam> selectBookmarkTopExams();
}