package com.example.examPlatform.form;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.Data;

/** 試験受験Form */
@Data
public class ExamActForm {
	/** 解答リスト */
	@Valid
	private List<AnsForm> ansList;
	
	public ExamActForm() {
		ansList = new ArrayList<>();
	}
	
	public void addAnsForm(Integer questionId) {
		ansList.add(new AnsForm(questionId));
	}
}
