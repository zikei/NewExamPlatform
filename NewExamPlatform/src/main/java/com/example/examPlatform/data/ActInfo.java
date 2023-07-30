package com.example.examPlatform.data;

import java.util.Date;
import java.util.List;

import com.example.examPlatform.entity.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 受験情報クラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActInfo {
	/** ユーザId */
	private Integer userId;
	
	/** 試験Id */
	private Integer examId;
	
	/** 受験開始日時 */
	private Date examDate;
	
	/** 全小問リスト */
	private List<Question> qList;
}