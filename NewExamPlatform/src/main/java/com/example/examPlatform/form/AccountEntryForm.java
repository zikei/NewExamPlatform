package com.example.examPlatform.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** ユーザForm */
@Data
public class AccountEntryForm {
	/** ユーザ名 半角英数字*/
	@NotBlank
	@Size(min=2, max=30)
	@Pattern(regexp="^[a-zA-Z0-9]+$")
	private String userName;
	
	/** パスワード 半角英数字記号*/
	@NotBlank
	@Pattern(regexp="^[!-~]+$")
	private String password;
}
