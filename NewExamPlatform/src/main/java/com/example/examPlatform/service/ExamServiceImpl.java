package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.ExamData;
import com.example.examPlatform.data.link.ExamLinkView;
import com.example.examPlatform.data.question.BigQuestionData;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.data.question.QuestionData;
import com.example.examPlatform.entity.BigQuestion;
import com.example.examPlatform.entity.Choices;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Ganre;
import com.example.examPlatform.entity.Question;
import com.example.examPlatform.entity.Tag;
import com.example.examPlatform.repository.BigQuestionRepository;
import com.example.examPlatform.repository.ChoicesRepository;
import com.example.examPlatform.repository.ExamRepository;
import com.example.examPlatform.repository.GanreRepository;
import com.example.examPlatform.repository.QuestionRepository;
import com.example.examPlatform.repository.TagRepository;

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
	
	@Autowired
	TagRepository tagRepo;
	
	@Autowired
	GanreRepository ganreRepo;
	
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
	public List<String> selectTag() {
		List<String> tagList = new ArrayList<>();
		tagRepo.findTag().forEach(tagList::add);
		return tagList;
	}
	
	@Override
	public List<String> selectTagByExamId(Integer examId) {
		List<String> tagList = new ArrayList<>();
		tagRepo.findByExamId(examId).forEach(t -> tagList.add(t.getTag()));
		return tagList;
	}

	@Override
	public List<Ganre> selectAllGanre() {
		List<Ganre> ganreList = new ArrayList<>();
		ganreRepo.findAll().forEach(ganreList::add);
		return ganreList;
	}
	
	@Override
	public void examRegister(ExamData examData, ExamQuestion examQuestion) {
		Exam registExam = insertExam(examData.getExam());
		
		Integer examId = registExam.getExamId();
		insertTag(examId, examData.getTagList());
		insertBigQuestionData(examId, examQuestion);
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
	
	/** DBにタグを保存 */
	private void insertTag(Integer examId, List<String> strTagList) {
		List<Tag> tagList = new ArrayList<>();
		strTagList.forEach(s -> tagList.add(new Tag(null, examId, s)));
		tagList.forEach(t -> tagRepo.save(t));
	}
	
	/** DBに大問、小問、選択肢を保存 */
	private void insertBigQuestionData(Integer examId, ExamQuestion examQuestion) {
		List<BigQuestionData> bqDataList = examQuestion.getBigQuestionList();
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
	private void insertChoicesList(Integer qId, List<Choices> cList) {
		for(Choices c : cList) {
			insertChoices(qId, c);
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
	private Choices insertChoices(Integer qId, Choices c) {
		c.setChoicesId(null);
		c.setQuestionId(qId);
		return cRepo.save(c);
	}
}