package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.link.ExamLinkView;
import com.example.examPlatform.data.question.BigQuestionData;
import com.example.examPlatform.data.question.QuestionData;
import com.example.examPlatform.entity.BigQuestion;
import com.example.examPlatform.entity.Choices;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Question;
import com.example.examPlatform.repository.BigQuestionRepository;
import com.example.examPlatform.repository.ChoicesRepository;
import com.example.examPlatform.repository.ExamRepository;
import com.example.examPlatform.repository.QuestionRepository;

/** 試験関連処理実装クラス　*/
@Service
@Transactional
public class ExamServiceImpl implements ExamService{
	@Autowired
	ExamRepository examRepo;
	
	@Autowired
	BigQuestionRepository bqRepo;
	
	@Autowired
	QuestionRepository qRepo;
	
	@Autowired
	ChoicesRepository cRepo;
	
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
	public void examRegister(Exam exam, List<BigQuestionData> questionData) {
		Exam registExam = insertExam(exam);
		Integer examId = registExam.getExamId();
		insertBigQuestionData(examId, questionData);
	}

	@Override
	public List<ExamLinkView> makeExamLinkList(List<Exam> examList) {
		List<ExamLinkView> examLinkList = new ArrayList<>();
		for(Exam exam : examList) {
			ExamLinkView addLink = new ExamLinkView(exam);
			
			examLinkList.add(addLink);
		}
		return examLinkList;
	}
	
	/** DBに試験概要を保存 */
	private Exam insertExam(Exam exam) {
		exam.setExamId(null);
		return examRepo.save(exam);
	}
	
	/** DBに大問、小問、選択肢を保存 */
	private void insertBigQuestionData(Integer examId, List<BigQuestionData> bqDataList) {
		for(BigQuestionData bqData : bqDataList) {
			BigQuestion registBq = insertBigQuestion(examId, bqData.getBigQuestion());
			Integer bqId = registBq.getBigQuestionId();
			insertQuestionData(bqId, bqData.getQuestionList());
		}
	}
	
	/** DBに小問、選択肢を保存 */
	private void insertQuestionData(Integer bqId, List<QuestionData> qDataList) {
		for(QuestionData qData : qDataList) {
			Question registQ = insertQuestion(bqId, qData.getQuestion());
			Integer qId = registQ.getQuestionId();
			insertChoicesList(qId, qData.getChoicesList());
		}
	}
	
	/** DBに選択肢リストを保存 */
	private void insertChoicesList(Integer bqId, List<Choices> cList) {
		for(Choices c : cList) {
			insertChoices(bqId, c);
		}
	}
	
	/** DBに大問を保存 */
	private BigQuestion insertBigQuestion(Integer examId, BigQuestion bq) {
		bq.setBigQuestionId(null);
		bq.setExamId(examId);
		return bqRepo.save(bq);
	}
	
	/** DBに小問を保存 */
	private Question insertQuestion(Integer bqId, Question q) {
		q.setQuestionId(null);
		q.setBigQuestionId(bqId);
		return qRepo.save(q);
	}
	
	/** DBに選択肢を保存 */
	private Choices insertChoices(Integer bqId, Choices c) {
		c.setChoicesId(null);
		c.setQuestionId(bqId);
		return cRepo.save(c);
	}
}