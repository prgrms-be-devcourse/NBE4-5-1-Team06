package com.team6.cafe.global.common.exception;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.team6.cafe.global.common.response.ErrorCode;
import com.team6.cafe.global.common.response.ErrorConstant;
import com.team6.cafe.global.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String EXCEPTION_FORMAT = "[EXCEPTION]                   -----> ";
	private static final String EXCEPTION_MESSAGE_FORMAT = "[EXCEPTION] EXCEPTION_MESSAGE -----> [{}]";
	private static final String EXCEPTION_TYPE_FORMAT = "[EXCEPTION] EXCEPTION_TYPE    -----> [{}]";

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(
		final BusinessException exception
	) {

		return ResponseEntity
			.status(exception.getErrorCode().getStatus().intValue())
			.body(ErrorResponse.from(exception.getErrorCode()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
		final IllegalArgumentException exception
	) {
		logWarn(exception);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.from(ErrorCode.BAD_REQUEST, exception.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		final MethodArgumentNotValidException exception
	) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		String messages = fieldErrors.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining("\n"));

		logWarn(exception);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.from(ErrorCode.BAD_REQUEST, messages));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleInternalException(
		final Exception exception
	) {
		logError(exception);

		return ResponseEntity
			.status(ErrorConstant.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER));
	}

	private void logError(Exception e) {
		log.error(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
		log.error(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
		log.error(EXCEPTION_FORMAT, e);
	}

	private void logWarn(Exception e) {
		log.warn(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
		log.warn(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
	}
}
