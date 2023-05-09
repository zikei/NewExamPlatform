package com.example.examPlatform.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Report;
import com.example.examPlatform.entity.ReportCount;

/**
 * Reportテーブル：リポジトリ
 */
public interface ReportRepository extends CrudRepository<Report, Integer> {
	/** ユーザIDで検索を行う */
	@Query("SELECT * FROM Bookmark WHERE UserId = :userId")
	Iterable<Report> findByUserId(Integer userId);
	
	/** 全試験の受験数を検索する */
	@Query("SELECT examid, Count(examid) AS reportcnt FROM report group by examid")
	Iterable<ReportCount> findCountAll();
	
	/** 試験の受験数を検索する */
	@Query("SELECT examid, Count(examid) AS reportcnt FROM report WHERE examId = :examId group by examid")
	ReportCount findCountByExamId(Integer examId);
}