package com.example.examPlatform.service;

import java.util.List;

import com.example.examPlatform.data.ExamLinkView;
import com.example.examPlatform.data.ReportLinkView;
import com.example.examPlatform.entity.Account;
import com.example.examPlatform.exception.NotFoundException;

/** マイページ関連処理 */
public interface MypageService {
	/** ユーザ情報取得 
	 * @throws NotFoundException */
	Account selectUser(String userName) throws NotFoundException;
	
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