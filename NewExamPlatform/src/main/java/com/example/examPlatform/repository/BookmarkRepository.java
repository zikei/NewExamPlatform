package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Bookmark;
import com.example.examPlatform.entity.BookmarkCount;

/**
 * Bookmarkテーブル：リポジトリ
 */
public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
	/** ユーザIDで検索を行う */
	@Query("SELECT * FROM Bookmark WHERE UserId = :userId")
	Iterable<Bookmark> findByUserId(Integer userId);
	
	/** 全試験のブックマーク数を検索する */
	@Query("SELECT examid, Count(examid) AS bookmarkcnt FROM Bookmark group by examid")
	Iterable<BookmarkCount> findCountAll();
	
	/** 試験のブックマーク数を検索する */
	@Query("SELECT examid, Count(examid) AS bookmarkcnt FROM Bookmark WHERE examId = :examId group by examid")
	BookmarkCount findCountByExamId(Integer examId);
}