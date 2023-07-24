package com.example.examPlatform.form;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 試験受験Form */
@Data
public class ExamActForm {
	/** やり直し試験 */
	@NotNull
	private boolean isRedo;
	
	/** 解答リスト */
	@Valid
	private List<AnsForm> ansList;
	
	public ExamActForm() {
		this.isRedo = false;
		ansList = new ArrayList<>();
	}
	
	public ExamActForm(boolean isRedo) {
		this.isRedo = isRedo;
		ansList = new ArrayList<>();
	}
	
	public void addAnsForm(Integer questionId) {
		ansList.add(new AnsForm(questionId));
	}
}
