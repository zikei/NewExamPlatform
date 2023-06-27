package com.example.examPlatform.form;

import java.util.ArrayList;
import java.util.List;

import com.example.examPlatform.entity.Question;

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
	private Integer point = 1;
	
	/** 選択肢フォームリスト */
	@Valid
	private List<ChoicesCreateForm> choicesFormList;
	
	
	public QuestionCreateForm() {
		choicesFormList = new ArrayList<>();
		choicesFormList.add(new ChoicesCreateForm());
	}
	
	public QuestionCreateForm(Question q, List<ChoicesCreateForm> choicesFormList) {
		this.questionNum         = q.getQuestionNum();
		this.questionSentence    = q.getQuestionSentence();
		this.questionAns         = q.getQuestionAns();
		this.questionExplanation = q.getQuestionExplanation();
		this.point               = q.getPoint();
		this.choicesFormList     = choicesFormList;
	}
	
	public void addChoices() {
		choicesFormList.add(new ChoicesCreateForm());
	}
	
	public void removeChoices(Integer index) {
		if(index == null) return;
		if(index < 0)     return;
		if(index >= choicesFormList.size()) return;
		
		int idx = index;
		try {
			choicesFormList.remove(idx);
			if(choicesFormList.isEmpty()) {
				addChoices();
			}
		}catch(IndexOutOfBoundsException e){}
	}
}
