package com.example.examPlatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.entity.Account;
import com.example.examPlatform.repository.AccountRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	@Autowired
	AccountRepository accountRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void userRegister(Account registUser) {
		registUser.setPassword(passwordEncoder.encode(registUser.getPassword()));
		registUser = accountRepo.save(registUser);
	}
	
}