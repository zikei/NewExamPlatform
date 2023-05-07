package com.example.examPlatform.entity;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reportテーブル：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCount {
	/** 試験ID */
	@Column(value="examid")
	private Integer examId;
	
	/** 受験数 */
	@Column(value="reportcnt")
	private Integer reportCnt;
}