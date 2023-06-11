package com.example.examPlatform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tagテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
	/** タグID:PK */
	@Id
	@Column(value="tagid")
	private Integer tagId;
	
	/** 試験ID:FK */
	@Column(value="examid")
	private Integer examId;
	
	/** タグ */
	@Column(value="tag")
	private String tag;
}
