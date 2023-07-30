package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.ExamData;
import com.example.examPlatform.data.link.ExamLinkView;
import com.example.examPlatform.data.question.BigQuestionData;
import com.example.examPlatform.data.question.BigQuestionView;
import com.example.examPlatform.data.question.ChoicesView;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.data.question.ExamQuestionView;
import com.example.examPlatform.data.question.QuestionData;
import com.example.examPlatform.data.question.QuestionView;
import com.example.examPlatform.entity.BigQuestion;
import com.example.examPlatform.entity.Choices;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Ganre;
import com.example.examPlatform.entity.Question;
import com.example.examPlatform.entity.Tag;
import com.example.examPlatform.exception.NotFoundException;
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
	public ExamQuestion selectExamQuestionByExamId(Integer examId) {
		List<BigQuestion> bqList = selectBQList(examId);
		List<BigQuestionData> bqdList = selectBigQuestionData(bqList);
		return new ExamQuestion(bqdList);
	}
	
	@Override
	public List<Exam> selectExamByUserID(Integer userId) {
		List<Exam> examList = new ArrayList<>();
		examRepo.findByUserId(userId).forEach(examList::add);
		return examList;
	}
	
	@Override
	public List<String> selectTagByExamId(Integer examId) {
		List<String> tagList = new ArrayList<>();
		tagRepo.findByExamId(examId).forEach(t -> tagList.add(t.getTag()));
		return tagList;
	}
	
	@Override
	public List<String> selectTag() {
		List<String> tagList = new ArrayList<>();
		tagRepo.findTag().forEach(tagList::add);
		return tagList;
	}
	
	@Override
	public List<Ganre> selectAllGanre() {
		List<Ganre> ganreList = new ArrayList<>();
		ganreRepo.findAll().forEach(ganreList::add);
		return ganreList;
	}
	
	@Override
	public Ganre selectGanreByGanreId(Integer ganreId) throws NotFoundException {
		return ganreRepo.findById(ganreId).orElseThrow(() -> new NotFoundException("NotFound GanreId:" + ganreId));
	}
	
	@Override
	public void examRegister(ExamData examData, ExamQuestion examQuestion) {
		Exam exam = examData.getExam();
		exam.setCreateDate(new Date());
		Exam registExam = insertExam(exam);
		
		Integer examId = registExam.getExamId();
		insertTag(examId, examData.getTagList());
		examQuestion.setExamId(examId);
		insertBigQuestionData(examQuestion);
	}

	@Override
	public void examUpdate(ExamData examData) {
		Exam exam = examData.getExam();
		exam.setUpdateDate(new Date());
		Integer examId = exam.getExamId();
		
		examRepo.save(exam);
		tagRepo.deleteByExamId(examId);
		insertTag(examId, examData.getTagList());
	}
	
	@Override
	public void examQuestionUpdate(ExamQuestion examQuestion) {
		Integer examId = examQuestion.getExamId();
		
		Exam exam = selectExamByExamId(examId).get();
		exam.setUpdateDate(new Date());
		examRepo.save(exam);
		
		// 問題を削除し更新した試験問題を登録する
		// 大問を削除するとその配下の小問・選択肢も制約により削除されるため大問を削除する
		bqRepo.deleteByExamId(examId);
		insertBigQuestionData(examQuestion);
	}
	
	@Override
	public void examDelete(Integer examId) {
		examRepo.deleteById(examId);
	}
	
	@Override
	public ExamQuestionView makeExamQuestionView(Exam exam){
		List<BigQuestionView> bqViewList = makeBigQuestionViewList(exam.getExamId());
		return new ExamQuestionView(exam, bqViewList);
	}
	
	@Override
	public List<Question> makeAllQuestionByExamId(Integer examId){
		List<Question> qList = new ArrayList<>();
		List<BigQuestion> bqList = selectBQList(examId);
		for(BigQuestion bq : bqList) {
			qList.addAll(selectQList(bq.getBigQuestionId()));
		}
		return qList;
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
	private void insertBigQuestionData(ExamQuestion examQuestion) {
		Integer examId = examQuestion.getExamId();
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
	
	/** 大門エンティティリスト取得 */
	private List<BigQuestion> selectBQList(Integer examId){
		List<BigQuestion> bqList = new ArrayList<>();
		bqRepo.findByExamId(examId).forEach(bq -> bqList.add(bq));
		bqList.sort(Comparator.comparing(BigQuestion::getBigQuestionNum));
		return bqList;
	}
	
	/** 小門エンティティリスト取得 */
	private List<Question> selectQList(Integer bqId){
		List<Question> qList = new ArrayList<>();
		qRepo.findByBigQuestionId(bqId).forEach(q -> qList.add(q));
		qList.sort(Comparator.comparing(Question::getQuestionNum));
		return qList;
	}
	
	/** 選択肢エンティティリスト取得 */
	private List<Choices> selectCList(Integer qId){
		List<Choices> cList = new ArrayList<>();
		cRepo.findByQuestionId(qId).forEach(c -> cList.add(c));
		cList.sort(Comparator.comparing(Choices::getChoicesNum));
		return cList;
	}
	
	/** 大問Data取得 */
	private List<BigQuestionData> selectBigQuestionData(List<BigQuestion> bqList){
		List<BigQuestionData> bqdList = new ArrayList<>();
		for(BigQuestion bq : bqList) {
			Integer bqId = bq.getBigQuestionId();
			List<Question> qList = selectQList(bqId);
			List<QuestionData> qdList = selectQuestionData(qList);
			bqdList.add(new BigQuestionData(bq, qdList));
		}
		return bqdList;
	}
	
	/** 小問Data取得 */
	private List<QuestionData> selectQuestionData(List<Question> qList){
		List<QuestionData> qdList = new ArrayList<>();
		for(Question q : qList) {
			Integer qId = q.getQuestionId();
			List<Choices> cList = selectCList(qId);
			qdList.add(new QuestionData(q, cList));
		}
		return qdList;
	}
	
	/** BigQuestionViewListを取得 */
	private List<BigQuestionView> makeBigQuestionViewList(Integer examId){
		List<BigQuestion> bqList = selectBQList(examId);
		List<BigQuestionView> bqViewList = new ArrayList<>();
		for(BigQuestion bq : bqList) {
			Integer bqId = bq.getBigQuestionId();
			List<Question> qList = selectQList(bqId);
			List<QuestionView> qViewList = makeQuestionViewList(qList);
			bqViewList.add(new BigQuestionView(bq, qViewList));
		}
		return bqViewList;
	}
	
	/** QuestionViewListを取得 */
	private List<QuestionView> makeQuestionViewList(List<Question> qList){
		List<QuestionView> qViewList = new ArrayList<>();
		for(Question q : qList) {
			Integer qId = q.getQuestionId();
			List<Choices> cList = selectCList(qId);
			List<ChoicesView> cViewList = makeChoicesViewList(cList);
			qViewList.add(new QuestionView(q, cViewList));
		}
		return qViewList;
	}
	
	/** ChoicesViewListを取得 */
	private List<ChoicesView> makeChoicesViewList(List<Choices> cList){
		List<ChoicesView> cViewList = new ArrayList<>();
		for(Choices c : cList) {
			cViewList.add(new ChoicesView(c));
		}
		return cViewList;
	}
}