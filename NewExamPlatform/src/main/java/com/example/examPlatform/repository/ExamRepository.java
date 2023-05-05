package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Exam;

/**
 * Examテーブル：リポジトリ
 */
public interface ExamRepository extends CrudRepository<Exam, Integer> {
	@Query("SELECT * FROM Exam WHERE examId = :examId")
	Exam findByExamId(Integer examId);
}