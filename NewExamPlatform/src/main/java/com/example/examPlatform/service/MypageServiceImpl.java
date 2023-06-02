package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.ExamLinkView;
import com.example.examPlatform.data.ReportLinkView;
import com.example.examPlatform.data.constant.DisclosureRange;
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
	@Autowired
	ExamService examService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	BookmarkRepository bookmarkRepo;
	
	@Autowired
	ReportRepository reportRepo;
	
	@Override
	public Account selectUser(String userName) throws NotFoundException {
		return accountService.selectAccountByUserName(userName);
	}

	@Override
	public String loginName() {
		return accountService.selectLoginUserName();
	}
	
	@Override
	public List<ExamLinkView> selectCreateExams(String userName) {
		List<ExamLinkView> createExamLinkList = new ArrayList<ExamLinkView>();
		DisclosureRange dr = new DisclosureRange();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String loginUserName = auth.getName();
		Account user;
		try {
			user = selectUser(userName);
		} catch (NotFoundException e) {
			return createExamLinkList;
		}
		
		List<Exam> createExamList = examService.selectExamByUserID(user.getUserId());
		
		if(!userName.equals(loginUserName)) {
			createExamList = 
					createExamList.stream().filter(exam -> dr.isOpen(exam.getDisclosureRange()))
					.collect(Collectors.toList());
		}
		
		createExamLinkList = examService.makeExamLinkList(createExamList);
		
		return createExamLinkList;
	}

	@Override
	public List<ExamLinkView> selectBookmarkExams(String userName) {
		List<ExamLinkView> bookmarkExamLinkList = new ArrayList<ExamLinkView>();
		Account user;
		try {
			user = selectUser(userName);
		} catch (NotFoundException e) {
			return bookmarkExamLinkList;
		}
		
		List<Bookmark> bookmarkList = new ArrayList<Bookmark>();
		bookmarkRepo.findByUserId(user.getUserId()).forEach(bookmarkList::add);
		
		List<Exam> bookmarkExamList = new ArrayList<Exam>();
		for(Bookmark bookmark : bookmarkList) {
			Optional<Exam> addExamOpt = examService.selectExamByExamId(bookmark.getExamId());
			addExamOpt.ifPresent(bookmarkExamList::add);
		}
		
		bookmarkExamLinkList = examService.makeExamLinkList(bookmarkExamList);
		return bookmarkExamLinkList;
	}

	@Override
	public List<ReportLinkView> selectReports(String userName) {
		List<ReportLinkView> reportLinkList = new ArrayList<ReportLinkView>();
		Account user;
		try {
			user = selectUser(userName);
		} catch (NotFoundException e) {
			return reportLinkList;
		}
		
		List<Report> reportList = new ArrayList<Report>();
		reportRepo.findByUserId(user.getUserId()).forEach(reportList::add);
		
		for(Report report : reportList) {
			ReportLinkView addLink = new ReportLinkView(report);
			
			reportLinkList.add(addLink);
		}
		
		return reportLinkList;
	}
}