package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.ExamData;
import com.example.examPlatform.data.question.BigQuestionData;
import com.example.examPlatform.data.question.ExamQuestion;
import com.example.examPlatform.data.question.QuestionData;
import com.example.examPlatform.entity.BigQuestion;
import com.example.examPlatform.entity.Choices;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Question;
import com.example.examPlatform.form.BigQuestionForm;
import com.example.examPlatform.form.ChoicesForm;
import com.example.examPlatform.form.ExamForm;
import com.example.examPlatform.form.ExamQuestionForm;
import com.example.examPlatform.form.QuestionCreateForm;
import com.example.examPlatform.form.TagForm;

/** 試験関連フォーム処理実装クラス　*/
@Service
@Transactional
public class ExamFormCtrServiceImpl implements ExamFormCtrService{
	@Autowired
	ExamService examService;
	
	@Autowired
	AccountService accountService;
	
	/** フォームに新規タグを追加 */
	@Override
	public void addTag(ExamForm examform) {
		examform.addTag();
	}
	
	/** フォームの指定されたタグを削除 */
	@Override
	public void removeTag(ExamForm examform, Integer index) {
		examform.removeTag(index);
	}
	
	/** フォームに新規大問を追加 */
	@Override
	public void addBQ(ExamQuestionForm questionForm) {
		questionForm.addBQ();
	}
	
	/** フォームの指定された大門を削除 */
	@Override
	public void removeBQ(ExamQuestionForm questionForm, Integer index) {
		questionForm.removeBQ(index);
	}
	
	/** フォームに新規小問を追加 */
	@Override
	public void addQ(ExamQuestionForm questionForm, Integer index) {
		questionForm.addQ(index);
	}
	
	/** フォームの指定された小問を削除 */
	@Override
	public void removeQ(ExamQuestionForm questionForm, String removeQ) throws NumberFormatException{
		Integer[] tmp = makeIntegerArray(removeQ);
		if(tmp.length == 2) {
			Integer bqidx = tmp[0];
			Integer qidx  = tmp[1];
			questionForm.removeQ(bqidx, qidx);
		}
	}
	
	/** フォームの指定された小問に選択肢を追加 */
	@Override
	public void addC(ExamQuestionForm questionForm, String addC) throws NumberFormatException{
		Integer[] tmp = makeIntegerArray(addC);
		if(tmp.length == 2) {
			Integer bqidx = tmp[0];
			Integer qidx  = tmp[1];
			questionForm.addChoices(bqidx, qidx);
		}
	}
	
	/** フォームの指定された選択肢を削除 */
	@Override
	public void removeC(ExamQuestionForm questionForm, String removeC) throws NumberFormatException{
		Integer[] tmp = makeIntegerArray(removeC);
		if(tmp.length == 3) {
			Integer bqidx = tmp[0];
			Integer qidx  = tmp[1];
			Integer cidx  = tmp[2];
			questionForm.removeChoices(bqidx, qidx, cidx);
		}
	}
	
	/** Examエンティティをformに変換 */
	@Override
	public ExamForm makeExamCreateForm(Exam exam) {
		List<String> tagList = examService.selectTagByExamId(exam.getExamId());
		List<TagForm> tagFormList = new ArrayList<>();
		tagList.forEach(s -> tagFormList.add(new TagForm(s)));
		
		ExamForm eForm = new ExamForm();
		eForm.setGenreId(exam.getGenreId());
		eForm.setExamName(exam.getExamName());
		eForm.setPassingScore(exam.getPassingScore());
		eForm.setExamTimeMinutes(exam.getExamTimeMinutes());
		eForm.setExamExplanation(exam.getExamExplanation());
		eForm.setDisclosureRange(exam.getDisclosureRange());
		eForm.setLimitedPassword(exam.getLimitedPassword());
		eForm.setQuestionFormat(exam.getQuestionFormat());
		eForm.setTagList(tagFormList);
		
		return eForm;
	}
	
	/** formをExamData形式に変換 */
	@Override
	public ExamData makeExamData(ExamForm eForm, Integer userId) {
		Exam exam = makeExam(eForm, userId);
		List<String> tagList = new ArrayList<>();
		eForm.getTagList().forEach(t -> tagList.add(t.getTag()));
		tagList.removeIf(s -> s==null || s.isBlank());
		
		ExamData examData = new ExamData(exam, tagList);
		return examData;
	}
	
	/** ExamQuestonをExamQuestionFormに変換 */
	@Override
	public ExamQuestionForm makeExamQuestionForm(ExamQuestion eq) {
		List<BigQuestionData> bqdList = eq.getBigQuestionList();
		List<BigQuestionForm> bqfList = makeBigQuestionForm(bqdList);
		return new ExamQuestionForm(bqfList);
	}
	
	/** ExamQuestionに変換 */
	@Override
	public ExamQuestion makeExamQuestion(ExamQuestionForm form){
		List<BigQuestionForm> bqFormList = form.getBigQuestionCreateForm();
		List<BigQuestionData> bqDataList = new ArrayList<BigQuestionData>();
		
		for(BigQuestionForm bqForm : bqFormList) {
			bqDataList.add(makeBigQuestionData(bqForm));
		}
		return new ExamQuestion(bqDataList);
	}
	
	/** 文字列をスペースで分割し分割した文字列をInteger型配列に変換する */
	private Integer[] makeIntegerArray(String str) throws NumberFormatException{
		String[] arrayStr = str.split(" ");
		Integer[] arrayInt = new Integer[arrayStr.length];
		for(int i = 0; i < arrayStr.length; i++) {
			arrayInt[i] = Integer.valueOf(arrayStr[i]);
		}
		return arrayInt;
	}
	
	/** formをExam形式に変換 */
	private Exam makeExam(ExamForm eForm, Integer userId) {
		
		Exam exam = new Exam();
		
		exam.setGenreId(eForm.getGenreId());
		exam.setExamName(eForm.getExamName());
		exam.setPassingScore(eForm.getPassingScore());
		exam.setExamTimeMinutes(eForm.getExamTimeMinutes());
		exam.setExamExplanation(eForm.getExamExplanation());
		exam.setDisclosureRange(eForm.getDisclosureRange());
		exam.setLimitedPassword(eForm.getLimitedPassword());
		exam.setQuestionFormat(eForm.getQuestionFormat());
		
		exam.setUserId(userId);
		return exam;
	}
	
	/** BigQuestionDataに変換 */
	private BigQuestionData makeBigQuestionData(BigQuestionForm form){
		BigQuestion bqEntity = makeBigQuestionEntity(form);
		
		List<QuestionCreateForm> qFormList = form.getQuestionCreateForm();
		List<QuestionData> qDataList = new ArrayList<QuestionData>();
			
		for(QuestionCreateForm qForm : qFormList) {
			qDataList.add(makeQuestionData(qForm));
		}
		
		return new BigQuestionData(bqEntity, qDataList);
	}
	
	/** QuestionDataに変換 */
	private QuestionData makeQuestionData(QuestionCreateForm form){
		Question qEntity = makeQuestionEntity(form);
		
		List<ChoicesForm> cFormList = form.getChoicesFormList();
		List<Choices> cList = new ArrayList<Choices>();
		
		for(ChoicesForm cForm : cFormList) {
			cList.add(makeChoicesEntity(cForm));
		}
		
		return new QuestionData(qEntity, cList);
	}
	
	/** 選択肢エンティティに変換 */
 	private Choices makeChoicesEntity(ChoicesForm cForm) {
		Choices c = new Choices();
		c.setChoicesNum(cForm.getChoicesNum());
		c.setChoices(cForm.getChoices());
		return c;
	}
	
	/** 小問エンティティに変換 */
	private Question makeQuestionEntity(QuestionCreateForm qForm) {
		Question q = new Question();
		q.setQuestionNum(qForm.getQuestionNum());
		q.setQuestionSentence(qForm.getQuestionSentence());
		q.setQuestionAns(qForm.getQuestionAns());
		q.setQuestionExplanation(qForm.getQuestionExplanation());
		q.setPoint(q.getPoint());
		return q;
	}
	
	/** 大問エンティティに変換 */
	private BigQuestion makeBigQuestionEntity(BigQuestionForm bqForm) {
		BigQuestion bq = new BigQuestion();
		bq.setBigQuestionNum(bqForm.getBigQuestionNum());
		bq.setBigQuestionSentence(bqForm.getBigQuestionSentence());
		return bq;
	}

	/** BigQuestionDataListをBigQuestionFormListに変換 */
	private List<BigQuestionForm> makeBigQuestionForm(List<BigQuestionData> bqdList) {
		List<BigQuestionForm> bqfList = new ArrayList<>();
		for(BigQuestionData bqd : bqdList) {
			List<QuestionData> qdList = bqd.getQuestionList();
			BigQuestion bq =bqd.getBigQuestion();
			
			List<QuestionCreateForm> qfList = makeQuestionForm(qdList);
			bqfList.add(new BigQuestionForm(bq, qfList));
		}
		return bqfList;
	}
	
	/** QuestionDataListをQuestionFormListに変換 */
	private List<QuestionCreateForm> makeQuestionForm(List<QuestionData> qdList) {
		List<QuestionCreateForm> qfList = new ArrayList<>();
		for(QuestionData qd : qdList) {
			List<Choices> cList = qd.getChoicesList();
			Question q =qd.getQuestion();
			
			List<ChoicesForm> cfList = makeChoicesForm(cList);
			qfList.add(new QuestionCreateForm(q, cfList));
		}
		return qfList;
	}
	
	/** ChoicesListをChoicesFormListに変換 */
	private List<ChoicesForm> makeChoicesForm(List<Choices> cList) {
		List<ChoicesForm> cfList = new ArrayList<>();
		for(Choices c : cList) {
			cfList.add(new ChoicesForm(c));
		}
		return cfList;
	}
}