package com.example.examPlatform.exception;

/** 採点時の例外 */
public class ScoringException extends Exception{
	public ScoringException() {
		super();
	}

	public ScoringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ScoringException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScoringException(String message) {
		super(message);
	}

	public ScoringException(Throwable cause) {
		super(cause);
	}
}