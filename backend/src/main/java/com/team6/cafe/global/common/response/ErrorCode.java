package com.team6.cafe.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	BAD_REQUEST(ErrorConstant.BAD_REQUEST, "GLOBAL-400", "잘못된 요청입니다."),
	INTERNAL_SERVER(ErrorConstant.INTERNAL_SERVER_ERROR, "GLOBAL-500", "서버 내부 오류입니다."),

	// ---- 주문 ---- //
	ORDER_NOT_FOUND(ErrorConstant.NOT_FOUND, "ORDER-001", "존재하지 않는 주문입니다."),

	// ---- 커피 ---- //
	COFFEE_NOT_FOUND(ErrorConstant.NOT_FOUND, "COFFEE-001", "존재하지 않는 커피입니다.");

	private final Integer status;
	private final String code;
	private final String message;
}
