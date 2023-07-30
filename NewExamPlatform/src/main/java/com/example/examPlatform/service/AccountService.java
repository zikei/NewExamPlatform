package com.example.examPlatform.service;

import com.example.examPlatform.entity.Account;
import com.example.examPlatform.exception.NotFoundException;

/** アカウント関連処理 */
public interface AccountService {
	/** ログインユーザか判定 */
	boolean isLoginUser(String userName);
	
	/** ログインユーザか判定 */
	boolean isLoginUser(Integer userId);
	
	/** ユーザ登録 */
	void userRegister(Account registUser);
	
	/** ユーザ退会　*/
	boolean userWithdrow(Integer userId, String password) throws NotFoundException;
	
	/** アカウントの情報を更新(パスワード以外) */
	void userInfoUpd(Account user) throws NotFoundException;
	
	/** パスワード更新 */
	boolean userPassUpd(Integer userId, String newPass, String oldPass) throws NotFoundException;
	
	/** アカウント取得 */
	Account selectAccountByUserId(Integer userId) throws NotFoundException ;
	
	/** ログインユーザ名取得 */
	String selectLoginUserName();
	
	/** ログインユーザId取得 */
	Integer selectLoginUserId();
	
	/** ユーザ名によるアカウント取得 */
	Account selectAccountByUserName(String userName) throws NotFoundException ;
}