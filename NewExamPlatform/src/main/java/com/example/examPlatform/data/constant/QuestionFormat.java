package com.example.examPlatform.data.constant;

/** 問題形式 */
public class QuestionFormat {
	/** 大問形式 */
	private final int sectionFormat = 0;
	/** 小問形式 */
	private final int qestionAnsFormat = 1;
	
	/** 大問形式判定：大問形式ならtrue */
	public boolean isSectionFormat(int questionFormat) {
		return this.sectionFormat == questionFormat;
	}
	
	/** 小問形式判定：小問形式ならtrue */
	public boolean isQuestionAnsFormat(int questionFormat) {
		return this.qestionAnsFormat == questionFormat;
	}

	
	public int getSectionFormat() {
		return sectionFormat;
	}
	
	public int getQuestionAnsFormat() {
		return qestionAnsFormat;
	}
}