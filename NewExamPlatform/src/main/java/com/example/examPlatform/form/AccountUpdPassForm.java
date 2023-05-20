package com.example.examPlatform.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** パスワード更新フォーム */
@Data
public class AccountUpdPassForm {
	/** 現在のパスワード 半角英数字記号*/
	@NotBlank
	@Pattern(regexp="^[!-~]+$", message="パスワードは半角英数字記号で入力してください")
	private String oldPassword;
	
	/** 新しいパスワード 半角英数字記号*/
	@NotBlank
	@Pattern(regexp="^[!-~]+$", message="パスワードは半角英数字記号で入力してください")
	private String newPassword;
	
	/** 確認用新しいパスワード 半角英数字記号*/
	@NotBlank
	@Pattern(regexp="^[!-~]+$", message="パスワードは半角英数字記号で入力してください")
	private String checkPassword;
}