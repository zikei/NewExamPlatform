package com.example.examPlatform.form;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** 小問作成フォーム */
@Data
public class QuestionCreateForm {
	/** 小問番号 */
	@NotNull
	private Integer questionNum;
	
	/** 問題文 */
	@NotBlank
	@Pattern(regexp="^[!-~]+$", message="問題文は半角英数字記号で入力してください")
	private String questionSentence;
	
	/** 正答選択肢ID:FK */
	@NotNull
	private Integer questionAns;
	
	/** 解説 */
	@Pattern(regexp="^[!-~]+$", message="解説は半角英数字記号で入力してください")
	private String questionExplanation;
	
	/** 配点 */
	@NotNull
	private Integer point;
	
	/** 選択肢フォームリスト */
	@Valid
	private List<ChoicesCreateForm> choicesFormList;
}
