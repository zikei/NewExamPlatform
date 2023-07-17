package com.example.examPlatform.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.examPlatform.form.AccountEntryForm;
import com.example.examPlatform.repository.AccountRepository;

@Component
public class AccountEntryValidator implements Validator {
	@Autowired
	AccountRepository accountRepo;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AccountEntryForm.class.isAssignableFrom(clazz);
	}
	
	/** ユーザ名がすでに登録されているかを検査する */
	@Override
	public void validate(Object target, Errors errors) {
		AccountEntryForm form = (AccountEntryForm) target;
		String userName = form.getUserName();
		
		if(userName == null) return;
		
		if(accountRepo.findByUserName(userName).isPresent()) {
			errors.rejectValue("userName", "com.example.examPlatform.validator.userNameDuplication.message");
		}
	}
}