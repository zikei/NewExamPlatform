package com.example.examPlatform.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	public Optional<Account> selectAccountByUserId(Integer userId) {
		Optional<Account> accountOpt = accountRepo.findById(userId);
		return accountOpt;
	}
	
	@Override
	public Optional<Account> selectLoginAccount() {
		return Optional.empty();
	}

	@Override
	public Optional<Account> selectAccountByUserName(String userName) {
		return accountRepo.findByUserName(userName);
	}
}