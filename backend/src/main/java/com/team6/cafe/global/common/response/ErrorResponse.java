package com.team6.cafe.global.common.response;

import lombok.Builder;

@Builder
public record ErrorResponse(Integer status, String code, String message) {
	public static ErrorResponse from(ErrorCode errorCode) {
		return ErrorResponse.builder()
			.status(errorCode.getStatus())
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
	}

	public static ErrorResponse from(ErrorCode errorCode, String message) {
		return ErrorResponse.builder()
			.status(errorCode.getStatus())
			.code(errorCode.getCode())
			.message(message)
			.build();
	}
}
