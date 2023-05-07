package com.example.examPlatform.service;

import java.util.List;
import java.util.Optional;

import com.example.examPlatform.entity.Exam;

/** 試験関連処理 */
public interface ExamService {
	/** 試験全件取得 */
	List<Exam> selectAllExam();
	
	/** 試験取得 */
	Optional<Exam> selectExamByExamId(Integer examId);
}
