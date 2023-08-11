package com.example.examPlatform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examPlatform.data.ActInfo;
import com.example.examPlatform.entity.Exam;
import com.example.examPlatform.entity.Question;
import com.example.examPlatform.entity.Report;
import com.example.examPlatform.entity.UserAnswer;
import com.example.examPlatform.exception.NotFoundException;
import com.example.examPlatform.exception.ScoringException;
import com.example.examPlatform.form.AnsForm;
import com.example.examPlatform.form.ExamActForm;
import com.example.examPlatform.repository.ReportRepository;
import com.example.examPlatform.repository.UserAnswerRepository;

/** 試験関連処理実装クラス　*/
@Service
@Transactional
public class ReportServiceImpl implements ReportService{
	@Autowired
	ReportRepository reportRepo;
	
	@Autowired
	UserAnswerRepository userAnsRepo;

	@Override
	public Report makeReport(ExamActForm actForm, ActInfo actInfo, Date endTime , Exam exam) throws ScoringException {
		List<Question> qList = actInfo.getQList();
		List<AnsForm> ansList = actForm.getAnsList();
		final Integer USER_ID = actInfo.getUserId();
		final Integer EXAM_ID = actInfo.getExamId();
		
		List<UserAnswer> userAnsList = new ArrayList<>();
		int score = 0;
		for(AnsForm ans : ansList) {
			Question q = qList.stream().filter(lq -> lq.getQuestionId() == ans.getQuestionId()).findFirst()
					.orElseThrow(() -> new ScoringException("NotFound scoringQID:" + ans.getQuestionId()));
			
			boolean tf  = false;
			if(ans.getUserAns() == q.getQuestionAns()) {
				tf = true;
				score += q.getPoint();
			}
			userAnsList.add(new UserAnswer(null, actInfo.getUserId(), null, q.getQuestionId(), ans.getUserAns(), tf));
		}
		
		Integer useTimeMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(endTime.getTime() - actInfo.getExamDate().getTime());
		boolean success = score >= exam.getPassingScore()?true:false;
		Report repo = reportRepo.save(
			new Report(null, USER_ID, EXAM_ID, actInfo.getExamDate(), score, useTimeMinutes, actForm.isRedo(), success));
		
		userAnsList.stream().forEach(ua -> ua.setReportId(repo.getReportId()));
		userAnsList.stream().forEach(ua -> userAnsRepo.save(ua));
		
		return repo;
	}

	@Override
	public Report selectReport(Integer repoId) throws NotFoundException {
		Optional<Report> repoOpt = reportRepo.findById(repoId);
		Report repo = repoOpt.orElseThrow(() -> new NotFoundException("Report NotFound Id: " + repoId));
		return repo;
	}
}