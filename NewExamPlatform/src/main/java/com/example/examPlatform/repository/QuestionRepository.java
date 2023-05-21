package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Question;

/**
 * Questionテーブル：リポジトリ
 */
public interface QuestionRepository extends CrudRepository<Question, Integer> {
	/** 大問IDで検索を行う */
	@Query("SELECT * FROM Question WHERE BigQuestionId = :bigQuestionId")
	Iterable<Question> findByBigQuestionId(Integer bigQuestionId);
}