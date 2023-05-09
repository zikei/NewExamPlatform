package com.example.examPlatform.service;

import java.util.List;

import com.example.examPlatform.data.AccountView;
import com.example.examPlatform.entity.Exam;

/** マイページ関連処理 */
public interface MypageService {
	/** ログインユーザ情報取得 */
	AccountView selectLoginUser();
	/** 作成試験取得 */
	List<Exam> selectCreateExams();
	
}
