package com.example.examPlatform.form;

import java.util.ArrayList;
import java.util.List;

import com.example.examPlatform.entity.BigQuestion;

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
	
	
	public BigQuestionCreateForm() {
		questionCreateForm = new ArrayList<>();
		questionCreateForm.add(new QuestionCreateForm());
	}
	
	public BigQuestionCreateForm(BigQuestion bq, List<QuestionCreateForm> questionCreateForm) {
		this.BigQuestionNum      = bq.getBigQuestionNum();
		this.BigQuestionSentence = bq.getBigQuestionSentence();
		this.questionCreateForm  = questionCreateForm;
	}
	
	public void addQ() {
		questionCreateForm.add(new QuestionCreateForm());
	}
	
	public void removeQ(Integer index) {
		if(!checkIndex(index)) return;
		
		int idx = index;
		try {
			questionCreateForm.remove(idx);
			if(questionCreateForm.isEmpty()) {
				addQ();
			}
		}catch(IndexOutOfBoundsException e){}
	}
	
	public void addChoices(Integer index) {
		if(!checkIndex(index)) return;
		questionCreateForm.get(index).addChoices();
	}
	
	public void removeChoices(Integer Qidx, Integer Cidx) {
		if(!checkIndex(Qidx)) return;
		questionCreateForm.get(Qidx).removeChoices(Cidx);
	}
	
	private boolean checkIndex(Integer index) {
		if(index == null) return false;
		if(index < 0)     return false;
		if(index >= questionCreateForm.size()) return false;
		
		return true;
	}
}
