package com.example.examPlatform.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 試験解答Form */
@Data
public class AnsForm {
	/** 小問id */
	@NotNull
	private Integer questionId;
	
	/** 解答選択肢Id */
	@NotNull
	private Integer userAns;
	
	public AnsForm(Integer questionId) {
		this.questionId = questionId;
	}
}
