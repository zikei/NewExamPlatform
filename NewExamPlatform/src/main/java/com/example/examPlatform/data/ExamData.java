package com.example.examPlatform.data;

import java.util.List;

import com.example.examPlatform.entity.Exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 試験情報クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamData {
	/** 試験エンティティ */
	private Exam exam;
	/** タグリスト */
	private List<String> tagList;
}
