package com.example.examPlatform.service;

import java.util.List;

import com.example.examPlatform.data.link.ExamLinkView;
import com.example.examPlatform.data.link.ReportLinkView;

/** マイページ関連処理 */
public interface MypageService {
	/** 
	 * 作成試験取得 
	 * ログインユーザ本人の場合は作成試験全件
	 * ログインユーザ本人以外の場合は公開された作成試験
	 */
	List<ExamLinkView> selectCreateExams(String userName);
	
	/** ブックマーク試験取得 */
	List<ExamLinkView> selectBookmarkExams(String userName);
	
	/** レポート取得 */
	List<ReportLinkView> selectReports(String userName);
}