package com.example.examPlatform.form;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** 大問作成フォーム */
@Data
public class BigQuestionCreateForm {
	/** 大問番号 */
	@NotNull
	private Integer BigQuestionNum;
	
	/** 問題文 */
	@NotBlank
	@Pattern(regexp="^[!-~]+$", message="問題文は半角英数字記号で入力してください")
	private String BigQuestionSentence;
	
	/** 小問フォームリスト */
	@Valid
	private List<QuestionCreateForm> questionCreateForm;
}
