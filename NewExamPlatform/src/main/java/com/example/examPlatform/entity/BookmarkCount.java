package com.example.examPlatform.entity;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bookmarkテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkCount {
	/** 試験ID */
	@Column(value="examid")
	private Integer examId;
	
	/** ブックマーク数 */
	@Column(value="bookmarkcnt")
	private Integer bookmarkCnt;
}