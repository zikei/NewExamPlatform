package com.example.examPlatform.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.examPlatform.form.AccountUpdPassForm;

@Component
public class AccountUpdPassValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return AccountUpdPassForm.class.isAssignableFrom(clazz);
	}
	
	/** パスワードの値と確認用パスワードの値が一致するか検査 */
	@Override
	public void validate(Object target, Errors errors) {
		AccountUpdPassForm form = (AccountUpdPassForm) target;
		if(!form.getNewPassword().equals(form.getCheckPassword())) {
			errors.rejectValue("checkPassword", "com.example.examPlatform.validator.AccountUpdPassValidator.message");
		}
	}
}
