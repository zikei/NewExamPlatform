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
import com.example.examPlatform.entity.Exam;
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
	public Optional<Account> selectLoginUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String userName = auth.getName();
	    Optional<Account> loginUser = accountService.selectAccountByUserName(userName);
	    
		return loginUser;
	}

	@Override
	public List<ExamLinkView> selectCreateExams() {
		List<ExamLinkView> createExamLinkList = new ArrayList<ExamLinkView>();
		Account loginUser = new Account();
		
		Optional<Account> loginUserOpt = selectLoginUser();
		if(loginUserOpt.isPresent()) {
			loginUser = loginUserOpt.get();
		}else {
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
		return null;
	}

	@Override
	public List<ReportLinkView> selectReports() {
		List<ReportLinkView> reportLinkList = new ArrayList<ReportLinkView>();
		return null;
	}
}