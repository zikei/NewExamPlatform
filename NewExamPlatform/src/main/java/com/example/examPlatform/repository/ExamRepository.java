package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Exam;

/**
 * Examテーブル：リポジトリ
 */
public interface ExamRepository extends CrudRepository<Exam, Integer> {
	/** ユーザ名で検索を行う */
	@Query("SELECT * FROM Exam WHERE UserId = :userId")
	Iterable<Exam> findByUserId(Integer userId);
}