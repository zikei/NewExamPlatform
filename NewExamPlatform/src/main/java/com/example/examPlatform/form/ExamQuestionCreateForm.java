package com.example.examPlatform.form;

import java.util.List;

import jakarta.validation.Valid;
import lombok.Data;

/** 試験問題作成フォーム */
@Data
public class ExamQuestionCreateForm {
	/** 大問フォームリスト */
	@Valid
	private List<BigQuestionCreateForm> bigQuestionCreateForm;
}
