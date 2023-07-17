package com.example.examPlatform.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * 認可済み限定公開試験クラス
 */
@Component
@SessionScope
public class AuthorizedExam {
	/** 認可済み試験IDリスト */
	private List<Integer> AuthorizedExamList;
	
	public AuthorizedExam() {
		AuthorizedExamList = new ArrayList<>();
	}
	
	/** 認可済み試験追加 */
	public void add(Integer examId) {
		AuthorizedExamList.add(examId);
	}
	
	/** 指定された試験が認可済みの場合Trueを返す */
	public boolean isAuthorized(Integer examId) {
		return 	AuthorizedExamList.contains(examId);
	}
}
