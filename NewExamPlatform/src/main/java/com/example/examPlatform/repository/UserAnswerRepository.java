package com.example.examPlatform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.UserAnswer;

/**
 * Reportテーブル：リポジトリ
 */
public interface UserAnswerRepository extends CrudRepository<UserAnswer, Integer> {
}