package com.cs.exceptions;

import com.fasterxml.jackson.annotation.JsonValue;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

	NO_DATA_FOUND("1001"),
		ILLEGAL_ARGUMENT("1003"),
	INTERNAL_ERROR("1004"),
	NOT_VALID_JSON("1005"),
	INVALID_PASSWORD("1007"),
	NO_DATA_RECIEVED("1008"),
		SYSTEM_ERROR("1010"),
	NO_ID_FOUND("1011");
	
	private String code;

	ErrorCode(String code) {
		this.code = code;
	}

	@JsonValue
	public String getCode() {
		return code;
	}

}
