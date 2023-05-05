package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Bookmark;
import com.example.examPlatform.entity.BookmarkCount;

/**
 * Bookmarkテーブル：リポジトリ
 */
public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
	@Query("SELECT * FROM Bookmark WHERE UserId = :userId")
	Iterable<Bookmark> findByUserId(Integer userId);
	
	@Query("SELECT examid, Count(examid) AS bookmarkcnt FROM Bookmark group by examid")
	Iterable<BookmarkCount> findCountAll();
	
	@Query("SELECT examid, Count(examid) AS bookmarkcnt FROM Bookmark WHERE examId = :examId group by examid")
	BookmarkCount findCountByExamId(Integer examId);
}