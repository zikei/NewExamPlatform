package com.example.examPlatform.service;

import java.util.List;
import java.util.Optional;

import com.example.examPlatform.data.ExamLinkView;
import com.example.examPlatform.data.ReportLinkView;
import com.example.examPlatform.entity.Account;

/** マイページ関連処理 */
public interface MypageService {
	/** ログインユーザ情報取得 */
	Optional<Account> selectLoginUser();
	
	/** 作成試験取得 */
	List<ExamLinkView> selectCreateExams();
	
	/** ブックマーク試験取得 */
	List<ExamLinkView> selectBookmarkExams();
	
	/** レポート取得 */
	List<ReportLinkView> selectReports();
}