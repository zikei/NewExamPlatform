package com.example.examPlatform.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.LoginInfo;
import com.example.examPlatform.entity.Account;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.repository.AccountRepository;

/** アカウント関連処理実装クラス　*/
@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	@Autowired
	LoginInfo loginInfo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public boolean isLoginUser(String userName) {
		if(userName == null) return false; 
		return userName.equals(selectLoginUserName());
	}

	@Override
	public boolean isLoginUser(Integer userId) {
		String userName;
		try {
			userName = selectAccountByUserId(userId).getUserName();
		} catch (NotFoundException e) {
			return false;
		}
		return isLoginUser(userName);
	}
	
	/** ユーザDB登録処理 */
	@Override
	public void userRegister(Account registUser) {
		registUser.setPassword(passwordEncoder.encode(registUser.getPassword()));
		accountRepo.save(registUser);
		loginInfo.makeLoginInfo(registUser);
	}
	
	@Override
	public boolean userWithdrow(Integer userId, String password) throws NotFoundException {
		if(!checkPass(userId, password)) return false;
		
		accountRepo.deleteById(userId);
		return true;
	}
	
	@Override
	public void userInfoUpd(Account user) throws NotFoundException {
		String pass = selectAccountByUserId(user.getUserId()).getPassword();
		user.setPassword(pass);
		accountRepo.save(user);
		loginInfo.makeLoginInfo(user);
	}

	@Override
	public boolean userPassUpd(Integer userId, String newPass, String oldPass) throws NotFoundException {
		if(!checkPass(userId, oldPass)) return false;
		
		Account user = selectAccountByUserId(userId);
		user.setPassword(passwordEncoder.encode(newPass));
		accountRepo.save(user);
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
		return loginInfo.getUserName();
	}
	
	@Override
	public Integer selectLoginUserId() {
		return loginInfo.getUserId();
	}

	@Override
	public Account selectAccountByUserName(String userName) throws NotFoundException {
		Optional<Account> accountOpt = accountRepo.findByUserName(userName);
		Account user = accountOpt.orElseThrow(() -> new NotFoundException("NotFound UserName: " + userName));
		return user;
	}
	
	private boolean checkPass(Integer userId, String pass) throws NotFoundException {
		String dbPassword = selectAccountByUserId(userId).getPassword();
		if(!passwordEncoder.matches(pass, dbPassword)) return false;
		return true;
	}
}