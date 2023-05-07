package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Account;

/**
 * Accountテーブル：リポジトリ
 */
public interface AccountRepository extends CrudRepository<Account, Integer> {
	/** ユーザ名で検索を行う */
	@Query("SELECT * FROM Account WHERE UserName = :userName")
	Account findByUserName(String userName);
}