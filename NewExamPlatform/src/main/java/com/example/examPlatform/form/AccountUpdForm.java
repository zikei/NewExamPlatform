package com.example.examPlatform.form;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** ユーザ更新Form */
@Data
public class AccountUpdForm {
	/** ユーザ名 半角英数字*/
	@Size(min=2, max=30)
	@Pattern(regexp="^[a-zA-Z0-9]+$", message="ユーザ名は半角英数字で入力してください")
	private String userName;
	
	/** プロフィール */
	@Size(max=500)
	private String profile;
	
	/** デフォルト試験結果情報使用可否 */
	private Boolean useInfoDefault;
}
