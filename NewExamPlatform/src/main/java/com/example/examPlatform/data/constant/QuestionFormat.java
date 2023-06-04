package com.example.examPlatform.data.constant;

/** 問題形式 */
public class QuestionFormat {
	/** 大問形式 */
	private final int bigQuestionFormat = 0;
	/** 小問形式 */
	private final int qestionFormat = 1;
	
	/** 大問形式判定：大問形式ならtrue */
	public boolean isBigQuestionFormat(int questionFormat) {
		return this.bigQuestionFormat == questionFormat;
	}
	
	/** 小問形式判定：小問形式ならtrue */
	public boolean isQuestionFormat(int questionFormat) {
		return this.qestionFormat == questionFormat;
	}
	
	
	public int getBigQuestionFormat() {
		return bigQuestionFormat;
	}

	public int getQestionFormat() {
		return qestionFormat;
	}
}