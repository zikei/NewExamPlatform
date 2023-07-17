package com.example.examPlatform.data.question;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 大問表示用クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionView {
	/** 試験Id */
	private Integer examId;
	
	/** 大問リスト */
	private List<BigQuestionData> bigQuestionList;
}
