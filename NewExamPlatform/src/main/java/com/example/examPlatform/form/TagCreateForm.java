package com.example.examPlatform.form;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** 選択肢登録フォーム */
@Data
public class TagCreateForm {
	/** タグ */
	@Pattern(regexp="^[!-~]+$", message="半角英数字記号で入力してください")
	private String tag;
}