package com.example.examPlatform.data;

import com.example.examPlatform.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 表示用アカウントクラス */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountView {/** ユーザ名*/
	private String userName;
	
	/** プロフィール */
	private String profile;
	
	public void makeAccountView(Account account) {
		this.userName = account.getUserName();
		this.profile = account.getProfile();
	}
}