package com.example.examPlatform.form;

import com.example.examPlatform.entity.Choices;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** 選択肢登録フォーム */
@Data
public class ChoicesCreateForm {
	/** 選択肢番号 */
	@NotNull
	private Integer choicesNum;
	
	/** 選択肢 */
	@NotBlank
	@Pattern(regexp="^[!-~]+$", message="半角英数字記号で入力してください")
	private String choices;
	
	
	public ChoicesCreateForm() {
	}
	
	public ChoicesCreateForm(Choices c) {
		this.choicesNum = c.getChoicesNum();
		this.choices    = c.getChoices();
	}
}