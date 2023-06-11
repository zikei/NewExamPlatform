package com.example.examPlatform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ganreテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ganre {
	/** ジャンルID:PK */
	@Id
	@Column(value="ganreid")
	private Integer ganreId;
	
	/** ジャンル名 */
	@Column(value="ganrename")
	private String ganreName;
}
