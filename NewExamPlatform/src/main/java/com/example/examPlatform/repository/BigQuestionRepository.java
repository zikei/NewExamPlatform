package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.BigQuestion;

/**
 * BigQuestionテーブル：リポジトリ
 */
public interface BigQuestionRepository extends CrudRepository<BigQuestion, Integer> {
	/** 試験IDで検索を行う */
	@Query("SELECT * FROM BigQuestion WHERE ExamId = :examId")
	Iterable<BigQuestion> findByExamId(Integer examId);
	
	/** 指定された試験の問題を削除 */
	@Query("DELETE FROM BigQuestion WHERE ExamId = :examId")
	void deleteByExamId(Integer examId);
}