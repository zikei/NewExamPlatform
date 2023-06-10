package com.example.examPlatform.data.question;

import java.util.List;

import com.example.examPlatform.entity.Choices;
import com.example.examPlatform.entity.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小問クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionData {
	/** 小問エンティティ */
	private Question question;
	/** 選択肢リスト */
	private List<Choices> choicesList;
}
