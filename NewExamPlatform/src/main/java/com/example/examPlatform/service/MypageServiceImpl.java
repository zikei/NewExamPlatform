package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.ExamLinkView;
import com.example.examPlatform.data.ReportLinkView;
import com.example.examPlatform.entity.Account;
import com.example.examPlatform.entity.Bookmark;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Report;
import com.example.examPlatform.exception.NotFoundException;
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
	AccountService accountService;
	
	@Autowired
	BookmarkRepository bookmarkRepo;
	
	@Autowired
	ReportRepository reportRepo;
	
	@Override
	public Account selectLoginUser() throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String userName = auth.getName();
	    
	    Optional<Account> loginUserOpt = accountService.selectAccountByUserName(userName);
	    Account loginUser = loginUserOpt.orElseThrow(() -> new NotFoundException("NotFound UserName: " + userName));
	    
		return loginUser;
	}

	@Override
	public List<ExamLinkView> selectCreateExams() {
		// ログインユーザ情報取得
		List<ExamLinkView> createExamLinkList = new ArrayList<ExamLinkView>();
		Account loginUser;
		try {
			loginUser = selectLoginUser();
		} catch (NotFoundException e) {
			return createExamLinkList;
		}
		
		List<Exam> createExamList = examService.selectExamByUserID(loginUser.getUserId());
		if(createExamList.size() > displayCnt) createExamList = createExamList.subList(0, displayCnt);
		createExamLinkList = examService.makeExamLinkList(createExamList);
		
		return createExamLinkList;
	}

	@Override
	public List<ExamLinkView> selectBookmarkExams() {
		List<ExamLinkView> bookmarkExamLinkList = new ArrayList<ExamLinkView>();
		Account loginUser;
		try {
			loginUser = selectLoginUser();
		} catch (NotFoundException e) {
			return bookmarkExamLinkList;
		}
		
		List<Bookmark> bookmarkList = new ArrayList<Bookmark>();
		bookmarkRepo.findByUserId(loginUser.getUserId()).forEach(bookmarkList::add);
		if(bookmarkList.size() > displayCnt) bookmarkList = bookmarkList.subList(0, displayCnt);
		
		List<Exam> bookmarkExamList = new ArrayList<Exam>();
		for(Bookmark bookmark : bookmarkList) {
			Optional<Exam> addExamOpt = examService.selectExamByExamId(bookmark.getExamId());
			addExamOpt.ifPresent(bookmarkExamList::add);
		}
		
		bookmarkExamLinkList = examService.makeExamLinkList(bookmarkExamList);
		return bookmarkExamLinkList;
	}

	@Override
	public List<ReportLinkView> selectReports() {
		List<ReportLinkView> reportLinkList = new ArrayList<ReportLinkView>();
		Account loginUser;
		try {
			loginUser = selectLoginUser();
		} catch (NotFoundException e) {
			return reportLinkList;
		}
		
		List<Report> reportList = new ArrayList<Report>();
		reportRepo.findByUserId(loginUser.getUserId()).forEach(reportList::add);
		if(reportList.size() > displayCnt) reportList = reportList.subList(0, displayCnt);
		
		for(Report report : reportList) {
			ReportLinkView addLink = new ReportLinkView();
			addLink.makeExamLinkView(report);
			
			reportLinkList.add(addLink);
		}
		
		return reportLinkList;
	}
}