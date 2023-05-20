package com.example.examPlatform.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** ユーザ退会Form */
@Data
public class AccountWithdrawForm {
	/** パスワード 半角英数字記号*/
	@NotBlank
	@Pattern(regexp="^[!-~]+$", message="パスワードは半角英数字記号で入力してください")
	private String password;
}
