package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Tag;

/**
 * Tagテーブル：リポジトリ
 */
public interface TagRepository extends CrudRepository<Tag, Integer> {
	/** 試験IDで検索を行う */
	@Query("SELECT * FROM Tag WHERE ExamId = :examId")
	Iterable<Tag> findByExamId(Integer examId);
	
	/** 重複なしの全タグを取得 */
	@Query("SELECT DISTINCT Tag FROM Tag")
	Iterable<String> findTag();
	
	/** 指定された試験のタグを削除 */
	@Query("DELETE FROM Tag WHERE ExamId = :examId")
	void deleteByExamId(Integer examId);
}