package com.example.examPlatform.form;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** 試験概要登録Form */
@Data
public class ExamForm {
	/** ジャンルID*/
	@NotNull
	private Integer genreId;
	
	/** 試験名 */
	@NotBlank
	@Pattern(regexp="^[!-~]+$", message="試験名は半角英数字記号で入力してください")
	private String examName;
	
	/** 合格点 */
	@NotNull
	private Integer passingScore;
	
	/** 試験時間(分) */
	@NotNull
	private Integer examTimeMinutes;
	
	/** 試験概要 */
	@Pattern(regexp="^[!-~]+$", message="試験概要は半角英数字記号で入力してください")
	private String examExplanation;
	
	/** 公開範囲 */
	@NotNull
	private Integer disclosureRange;
	
	/** 限定公開パスワード */
	@Pattern(regexp="^[!-~]+$", message="限定公開用のパスワードは半角英数字記号で入力してください")
	private String limitedPassword;
	
	/** 問題形式 */
	@NotNull
	private Integer questionFormat;
	
	/** タグリスト */
	@Valid
	private List<TagForm> tagList;
	
	public ExamForm() {
		tagList = new ArrayList<>();
		tagList.add(new TagForm());
	}
	
	public void addTag() {
		tagList.add(new TagForm());
	}
	
	public void removeTag(Integer index) {
		if(index == null) return;
		if(index < 0)     return;
		if(index >= tagList.size()) return;
		
		int idx = index;
		tagList.remove(idx);
		if(tagList.isEmpty()) {
			addTag();
		}
	}
}