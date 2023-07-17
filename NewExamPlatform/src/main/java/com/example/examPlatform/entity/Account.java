package com.example.examPlatform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Memberテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
	/** ユーザID:PK */
	@Id
	@Column(value="userid")
	private Integer userId;
	
	/** ユーザ名:unique */
	@Column(value="username")
	private String userName;
	
	/** パスワード */
	@Column(value="password")
	private String password;
	
	/** プロフィール */
	@Column(value="profile")
	private String profile;
}