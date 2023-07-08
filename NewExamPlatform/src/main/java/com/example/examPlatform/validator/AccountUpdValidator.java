package com.example.examPlatform.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.examPlatform.form.AccountUpdForm;
import com.example.examPlatform.repository.AccountRepository;

@Component
public class AccountUpdValidator implements Validator {
	@Autowired
	AccountRepository accountRepo;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AccountUpdForm.class.isAssignableFrom(clazz);
	}
	
	/** ユーザ名がすでに登録されているかを検査する */
	@Override
	public void validate(Object target, Errors errors) {
		AccountUpdForm form = (AccountUpdForm) target;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginUserName = auth.getName();
		
		String userName = form.getUserName();
		
		if(userName == null) return;
		if(userName.equals(loginUserName)) return;
		if(accountRepo.findByUserName(userName).isPresent()) {
			System.out.println("yes");
			errors.rejectValue("userName", "com.example.examPlatform.validator.userNameDuplication.message");
		}
	}
}