package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.entity.BookmarkCount;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.repository.BookmarkRepository;

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
	
	/** 新着順試験リスト取得 */
	@Override
	public List<Exam> selectNewArrivalsExams() {
		List<Exam> examList = examService.selectAllExam();
		examList.sort(Comparator.comparing(Exam::getCreateDate).reversed());
		
		if(examList.size() > displayCnt) { 
			examList = examList.subList(0, displayCnt);
		}
		
		return examList;
	}

	/** 月間受験数順試験リスト取得 */
	@Override
	public List<Exam> selectMonthlyExeTopExams() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/** ブックマーク数順試験リスト取得 */
	@Override
	public List<Exam> selectBookmarkTopExams() {
		List<BookmarkCount> bookmarkCntList = new ArrayList<BookmarkCount>();
		bookmarkRepo.findCountAll().forEach(bookmarkCntList::add);
		bookmarkCntList.sort(Comparator.comparing(BookmarkCount::getBookmarkCnt).reversed());
		
		if(bookmarkCntList.size() > displayCnt) { 
			bookmarkCntList = bookmarkCntList.subList(0, displayCnt);
		}
		
		List<Exam> examList = new ArrayList<Exam>();
		for(BookmarkCount bookmarkCount : bookmarkCntList) {
			Exam addExam = examService.selectExamByExamId(bookmarkCount.getExamId());
			examList.add(addExam);
		}
		
		return examList;
	}
}
