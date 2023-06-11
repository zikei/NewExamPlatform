package com.example.examPlatform.form;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 選択肢登録フォーム */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagCreateForm {
	/** タグ */
	@Pattern(regexp="^[!-~]+$", message="半角英数字記号で入力してください")
	private String tag;
}