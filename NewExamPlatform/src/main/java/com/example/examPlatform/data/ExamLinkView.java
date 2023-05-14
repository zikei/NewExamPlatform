package com.example.examPlatform.data;

import com.example.examPlatform.entity.Exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 試験リンク表示用試験クラス */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamLinkView {
	/** 試験ID */
	private Integer examId;
	
	/** 試験名 */
	private String examName;
	
	public void makeExamLinkView(Exam exam) {
		this.examId = exam.getExamId();
		this.examName = exam.getExamName();
	}
}