package com.example.examPlatform.data.question;

import com.example.examPlatform.entity.Choices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 選択肢表示用クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoicesView {
	/** 選択肢ID */
	private Integer choicesId;
	
	/** 選択肢番号 */
	private Integer choicesNum;
	
	/** 選択肢 */
	private String choices;
	
	public ChoicesView(Choices c) {
		this.choicesId  = c.getChoicesId();
		this.choicesNum = c.getChoicesNum();
		this.choices    = c.getChoices();
	}
}
