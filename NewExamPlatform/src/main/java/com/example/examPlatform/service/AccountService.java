package com.example.examPlatform.service;

import java.util.Optional;

import com.example.examPlatform.entity.Account;

/** アカウント関連処理 */
public interface AccountService {
	/** アカウント取得 */
	Optional<Account> selectAccountByUserId(Integer userId);
	
	/** ログインユーザ情報取得 */
	Optional<Account> selectLoginAccount();
	
	/** ユーザ名によるアカウント取得 */
	Optional<Account> selectAccountByUserName(String userName);
}
