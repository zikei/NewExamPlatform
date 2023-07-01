package com.example.examPlatform.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.examPlatform.data.constant.DisclosureRange;
import com.example.examPlatform.form.ExamForm;

@Component
public class ExamValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return ExamForm.class.isAssignableFrom(clazz);
	}
	
	/** 限定公開の場合パスワードが入力されているかチェック */
	@Override
	public void validate(Object target, Errors errors) {
		ExamForm form = (ExamForm) target;
		DisclosureRange dr = new DisclosureRange();
		if(!dr.isLimited(form.getDisclosureRange())) return;
		
		if(form.getLimitedPassword() == null) {
			errors.rejectValue("limitedPassword", "com.example.examPlatform.validator.ExamValidator.message");
		}
	}
}