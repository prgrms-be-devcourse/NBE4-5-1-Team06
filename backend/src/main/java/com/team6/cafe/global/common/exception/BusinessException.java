package com.team6.cafe.global.common.exception;

import com.team6.cafe.global.common.response.ErrorCode;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());

		this.errorCode = errorCode;
	}
}
