package com.example.examPlatform.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.Login;
import com.example.examPlatform.entity.Account;

@Service
public class DbUserDetailsService implements UserDetailsService {
	@Autowired
	AccountService accountService;
	
	@Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<Account> userOpt = accountService.selectAccountByUserName(userName);
        Account user = userOpt.orElseThrow(() -> new UsernameNotFoundException("NotFound UserName: " + userName));

        return new Login(user, AuthorityUtils.createAuthorityList("ROLE_USER"));
    }
}
