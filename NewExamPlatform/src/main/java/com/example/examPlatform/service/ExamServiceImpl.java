package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.ExamLinkView;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.repository.ExamRepository;

/** 試験関連処理実装クラス　*/
@Service
@Transactional
public class ExamServiceImpl implements ExamService{
	@Autowired
	ExamRepository examRepo;
	
	/** 試験全件取得 */
	@Override
	public List<Exam> selectAllExam() {
		List<Exam> examList = new ArrayList<>();
		examRepo.findAll().forEach(examList::add);
		
		return examList;
	}
	
	/** 試験取得 */
	@Override
	public Optional<Exam> selectExamByExamId(Integer examId) {
		Optional<Exam> examOpt = examRepo.findById(examId);
		return examOpt;
	}
	
	@Override
	public List<Exam> selectExamByUserID(Integer userId) {
		List<Exam> examList = new ArrayList<>();
		examRepo.findByUserId(userId).forEach(examList::add);
		return examList;
	}

	@Override
	public List<ExamLinkView> makeExamLinkList(List<Exam> examList) {
		List<ExamLinkView> examLinkList = new ArrayList<>();
		for(Exam exam : examList) {
			ExamLinkView addLink = new ExamLinkView();
			addLink.makeExamLinkView(exam);
			
			examLinkList.add(addLink);
		}
		return examLinkList;
	}
}