package com.example.examPlatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.AccountView;
import com.example.examPlatform.data.ExamLinkView;
import com.example.examPlatform.data.ReportLinkView;
import com.example.examPlatform.repository.BookmarkRepository;
import com.example.examPlatform.repository.ReportRepository;

/** マイページ関連処理実装クラス　*/
@Service
@Transactional
public class MypageServiceImpl implements MypageService{
	/** マイページ上での表示件数 */
	private final int displayCnt = 20;
	
	@Autowired
	ExamService examService;
	
	@Autowired
	BookmarkRepository bookmarkRepo;
	
	@Autowired
	ReportRepository reportRepo;
	
	@Override
	public AccountView selectLoginUser() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<ExamLinkView> selectCreateExams() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<ExamLinkView> selectBookmarkExams() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<ReportLinkView> selectReports() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}