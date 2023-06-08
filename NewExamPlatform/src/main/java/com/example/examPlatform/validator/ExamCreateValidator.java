package com.example.examPlatform.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.examPlatform.data.constant.DisclosureRange;
import com.example.examPlatform.form.ExamCreateForm;

@Component
public class ExamCreateValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return ExamCreateForm.class.isAssignableFrom(clazz);
	}
	
	/** 限定公開の場合パスワードが入力されているかチェック */
	@Override
	public void validate(Object target, Errors errors) {
		ExamCreateForm form = (ExamCreateForm) target;
		DisclosureRange dr = new DisclosureRange();
		if(!dr.isLimited(form.getDisclosureRange())) return;
		
		if(form.getLimitedPassword() == null) {
			errors.rejectValue("limitedPassword", "com.example.examPlatform.validator.ExamCreateValidator.message");
		}
	}
}