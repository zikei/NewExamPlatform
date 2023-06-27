package com.example.examPlatform.form;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.Data;

/** 試験問題作成フォーム */
@Data
public class ExamQuestionCreateForm {
	/** 大問フォームリスト */
	@Valid
	private List<BigQuestionCreateForm> bigQuestionCreateForm;
	
	
	public ExamQuestionCreateForm() {
		bigQuestionCreateForm = new ArrayList<>();
		bigQuestionCreateForm.add(new BigQuestionCreateForm());
	}
	
	public ExamQuestionCreateForm(List<BigQuestionCreateForm> bigQuestionCreateForm) {
		this.bigQuestionCreateForm = bigQuestionCreateForm;
	}
	
	public void addBQ() {
		bigQuestionCreateForm.add(new BigQuestionCreateForm());
	}
	
	public void removeBQ(Integer index) {
		if(!checkIndex(index)) return;
		
		int idx = index;
		try {
			bigQuestionCreateForm.remove(idx);
			if(bigQuestionCreateForm.isEmpty()) {
				addBQ();
			}
		}catch(IndexOutOfBoundsException e){}
	}
	
	public void addQ(Integer index) {
		if(!checkIndex(index)) return;
		bigQuestionCreateForm.get(index).addQ();
	}
	
	public void removeQ(Integer BQidx, Integer Qidx) {
		if(!checkIndex(BQidx)) return;
		bigQuestionCreateForm.get(BQidx).removeQ(Qidx);
	}
	
	public void addChoices(Integer BQidx, Integer Qidx) {
		if(!checkIndex(BQidx)) return;
		bigQuestionCreateForm.get(BQidx).addChoices(Qidx);
	}
	
	public void removeChoices(Integer BQidx, Integer Qidx, Integer Cidx) {
		if(!checkIndex(BQidx)) return;
		bigQuestionCreateForm.get(BQidx).removeChoices(Qidx, Cidx);
	}
	
	private boolean checkIndex(Integer index) {
		if(index == null) return false;
		if(index < 0)     return false;
		if(index >= bigQuestionCreateForm.size()) return false;
		
		return true;
	}
}
