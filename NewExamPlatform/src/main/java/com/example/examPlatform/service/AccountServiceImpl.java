package com.example.examPlatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.entity.Account;
import com.example.examPlatform.repository.AccountRepository;

/** アカウント関連処理実装クラス　*/
@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	@Autowired
	AccountRepository accountRepo;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	/** ユーザDB登録処理 */
	@Override
	public Account userRegister(Account registUser) {
		registUser.setPassword(passwordEncoder.encode(registUser.getPassword()));
		registUser = accountRepo.save(registUser);
		return registUser;
	}
	
}