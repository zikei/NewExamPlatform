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
	/** ユーザID */
	@Id
	@Column(value="userid")
	private Integer userId;
	
	/** ユーザ名(ユニーク) */
	@Column(value="username")
	private String userName;
	
	/** パスワード */
	@Column(value="password")
	private String password;
	
	/** プロフィール */
	@Column(value="profile")
	private String profile;
	
	/** デフォルト試験結果情報使用可否 */
	@Column(value="useinfodefault")
	private Boolean useInfoDefault;
}