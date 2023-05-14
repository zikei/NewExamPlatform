package com.example.examPlatform.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.entity.Account;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.repository.AccountRepository;

/** アカウント関連処理実装クラス　*/
@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	@Autowired
	AccountRepository accountRepo;

	@Override
	public Account selectAccountByUserId(Integer userId) throws NotFoundException {
		Optional<Account> accountOpt = accountRepo.findById(userId);
	    Account user = accountOpt.orElseThrow(() -> new NotFoundException("NotFound UserId: " + userId));
		return user;
	}
	
	@Override
	public String selectLoginUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	@Override
	public Account selectAccountByUserName(String userName) throws NotFoundException {
		Optional<Account> accountOpt = accountRepo.findByUserName(userName);
		Account user = accountOpt.orElseThrow(() -> new NotFoundException("NotFound UserName: " + userName));
		return user;
	}
}