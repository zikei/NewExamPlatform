package com.example.examPlatform.service;

import java.util.List;

import com.example.examPlatform.data.ExamLinkView;

/** ホーム関連処理 */
public interface HomeService {
	/** 新着順試験取得 */
	List<ExamLinkView> selectNewArrivalsExams();
	
	/** 月間受験上位試験取得 */
	List<ExamLinkView> selectMonthlyExeTopExams();
	
	/** ブックマーク数順試験取得 */
	List<ExamLinkView> selectBookmarkTopExams();
}