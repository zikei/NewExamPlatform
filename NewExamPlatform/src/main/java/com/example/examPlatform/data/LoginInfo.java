package com.example.examPlatform.data;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.example.examPlatform.entity.Account;

import lombok.Data;

/**
 * ユーザId・ユーザ名格納クラス
 */
@Component
@SessionScope
@Data
public class LoginInfo {
	/** ユーザID */
	private Integer userId;
	
	/** ユーザ名 */
	private String userName;
	
	public void makeLoginInfo(Account account) {
		userId   = account.getUserId();
		userName = account.getUserName();
	}
}
