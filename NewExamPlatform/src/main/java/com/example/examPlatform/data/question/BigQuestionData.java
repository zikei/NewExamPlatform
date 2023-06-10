package com.example.examPlatform.data.question;

import java.util.List;

import com.example.examPlatform.entity.BigQuestion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 大問クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BigQuestionData {
	/** 大問エンティティ */
	private BigQuestion bigQuestion;
	/** 小問リスト */
	private List<QuestionData> questionList;
}
