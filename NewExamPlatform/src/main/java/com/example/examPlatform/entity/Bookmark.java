package com.example.examPlatform.entity;

import org.springframework.data.annotation.Id;
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
public class Bookmark {
	/** ブックマークID:PK */
	@Id
	@Column(value="bookmarkid")
	private Integer bookmarkId;
	
	/** ユーザID */
	@Column(value="userid")
	private Integer userId;
	
	/** 試験ID */
	@Column(value="examid")
	private Integer examId;
}