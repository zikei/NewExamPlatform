package com.example.examPlatform.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/** ユーザDB登録処理 */
	@Override
	public void userRegister(Account registUser) {
		registUser.setPassword(passwordEncoder.encode(registUser.getPassword()));
		accountRepo.save(registUser);
	}
	
	@Override
	public boolean userWithdrow(Integer userId, String password) throws NotFoundException {
		//パスワードチェック
		String dbPassword = selectAccountByUserId(userId).getPassword();
		if(!passwordEncoder.matches(password, dbPassword)) return false;
		
		accountRepo.deleteById(userId);
		return true;
	}

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