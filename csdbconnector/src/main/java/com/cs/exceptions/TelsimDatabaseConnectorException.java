package com.cs.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TelsimDatabaseConnectorException extends Exception {

	private final static Logger LOGGER = LoggerFactory.getLogger(TelsimDatabaseConnectorException.class);

	private static final long serialVersionUID = 1L;
	private ErrorType errorType;
	private String description;
	private ErrorCode errorCode;

	public TelsimDatabaseConnectorException(String message) {
		super(message);
	}

	public TelsimDatabaseConnectorException(String message, ErrorCode errorCode, ErrorType errorType,
			Throwable cause) {
		super(message, cause);
		this.description = message;
		this.errorCode = errorCode;
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public String getDescription() {
		return description;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
