package com.example.examPlatform.data;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.examPlatform.entity.Account;

/** ログイン用ユーザ情報 */
public class Login extends User{
	private Integer userId;
	public Login(Account user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getUserName(), user.getPassword(), authorities);
		userId = user.getUserId();
	}
	
	public Integer getUserId() {
		return userId;
	}
}
