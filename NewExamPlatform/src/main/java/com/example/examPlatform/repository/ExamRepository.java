package com.example.examPlatform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Exam;

/**
 * Examテーブル：リポジトリ
 */
public interface ExamRepository extends CrudRepository<Exam, Integer> {
	
}