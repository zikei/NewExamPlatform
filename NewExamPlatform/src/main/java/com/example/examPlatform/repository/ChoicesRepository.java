package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Choices;

/**
 * Choicesテーブル：リポジトリ
 */
public interface ChoicesRepository extends CrudRepository<Choices, Integer> {
	/** 小問IDで検索を行う */
	@Query("SELECT * FROM Choices WHERE QuestionId = :questionId")
	Iterable<Choices> findByQuestionId(Integer questionId);
}