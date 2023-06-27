package com.example.examPlatform.exception;

/** 要求した資源に対してアクセスできない場合の例外 */
public class ResourceAccessException extends Exception{
	public ResourceAccessException() {
		super();
	}

	public ResourceAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourceAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceAccessException(String message) {
		super(message);
	}

	public ResourceAccessException(Throwable cause) {
		super(cause);
	}
}