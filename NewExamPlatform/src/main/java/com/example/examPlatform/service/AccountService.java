package com.example.examPlatform.service;

import com.example.examPlatform.entity.Account;
import com.example.examPlatform.exception.NotFoundException;

/** アカウント関連処理 */
public interface AccountService {
	/** アカウント取得 */
	Account selectAccountByUserId(Integer userId) throws NotFoundException ;
	
	/** ログインユーザ情報取得 */
	String selectLoginUserName();
	
	/** ユーザ名によるアカウント取得 */
	Account selectAccountByUserName(String userName) throws NotFoundException ;
}
