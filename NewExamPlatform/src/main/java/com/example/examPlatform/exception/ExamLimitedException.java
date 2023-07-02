package com.example.examPlatform.exception;

/** 限定公開試験にアクセスした場合の例外 */
public class ExamLimitedException extends Exception{
	public ExamLimitedException() {
		super();
	}

	public ExamLimitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExamLimitedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExamLimitedException(String message) {
		super(message);
	}

	public ExamLimitedException(Throwable cause) {
		super(cause);
	}
}