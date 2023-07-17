package com.example.examPlatform.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.examPlatform.data.LoginInfo;
import com.example.examPlatform.entity.Account;
import com.example.examPlatform.repository.AccountRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	LoginInfo loginInfo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		Integer userId = Integer.parseInt(auth.getName());
		Account loginUser = accountRepo.findById(userId).get();
		loginInfo.makeLoginInfo(loginUser);
		
		response.sendRedirect("/ExamPlatform/" + loginInfo.getUserName());
	}

}
