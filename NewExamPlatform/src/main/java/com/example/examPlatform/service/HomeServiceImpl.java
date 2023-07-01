package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.ExamLinkView;
import com.example.examPlatform.entity.BookmarkCount;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.ReportCount;
import com.example.examPlatform.repository.BookmarkRepository;
import com.example.examPlatform.repository.ReportRepository;

/** ホーム関連処理実装クラス　*/
@Service
@Transactional
public class HomeServiceImpl implements HomeService{
	/** 表示件数 */
	private final int displayCnt = 20;
	
	@Autowired
	ExamService examService;
	
	@Autowired
	BookmarkRepository bookmarkRepo;
	
	@Autowired
	ReportRepository reportRepo;
	
	/** 新着順試験リスト取得 */
	@Override
	public List<ExamLinkView> selectNewArrivalsExams() {
		List<Exam> examList = examService.selectAllExam();
		examList.sort(Comparator.comparing(Exam::getCreateDate).reversed());
		
		if(examList.size() > displayCnt) examList = examList.subList(0, displayCnt);
		
		return examService.makeExamLinkList(examList);
	}

	/** 月間受験数順試験リスト取得 */
	@Override
	public List<ExamLinkView> selectMonthlyExeTopExams() {
		List<ReportCount> reportCntList = new ArrayList<ReportCount>();
		reportRepo.findCountAll().forEach(reportCntList::add);
		reportCntList.sort(Comparator.comparing(ReportCount::getReportCnt).reversed());
		
		if(reportCntList.size() > displayCnt) reportCntList = reportCntList.subList(0, displayCnt);
		
		List<Exam> examList = new ArrayList<Exam>();
		for(ReportCount reportCount : reportCntList) {
			Optional<Exam> addExamOpt = examService.selectExamByExamId(reportCount.getExamId());
			addExamOpt.ifPresent(examList::add);
		}
		
		return examService.makeExamLinkList(examList);
	}

	/** ブックマーク数順試験リスト取得 */
	@Override
	public List<ExamLinkView> selectBookmarkTopExams() {
		List<BookmarkCount> bookmarkCntList = new ArrayList<BookmarkCount>();
		bookmarkRepo.findCountAll().forEach(bookmarkCntList::add);
		bookmarkCntList.sort(Comparator.comparing(BookmarkCount::getBookmarkCnt).reversed());
		
		if(bookmarkCntList.size() > displayCnt) bookmarkCntList = bookmarkCntList.subList(0, displayCnt);
		
		List<Exam> examList = new ArrayList<Exam>();
		for(BookmarkCount bookmarkCount : bookmarkCntList) {
			Optional<Exam> addExamOpt = examService.selectExamByExamId(bookmarkCount.getExamId());
			addExamOpt.ifPresent(examList::add);
		}
		
		return examService.makeExamLinkList(examList);
	}
}