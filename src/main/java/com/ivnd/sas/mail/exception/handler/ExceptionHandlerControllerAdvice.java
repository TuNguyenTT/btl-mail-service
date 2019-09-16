package com.ivnd.sas.mail.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.ivnd.sas.mail.exception.AccessDeniedException;
import com.ivnd.sas.mail.exception.DuplicatedException;
import com.ivnd.sas.mail.exception.InputInvalidException;
import com.ivnd.sas.mail.exception.NotFoundEntityException;
import com.ivnd.sas.mail.model.ErrorDetail;

import lombok.extern.slf4j.Slf4j;

/**
 * Author : duybv
 * Feb 24, 2019
 */

@ControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice {

	private static final String INTERNAL_ERROR_MESSAGE = "Lỗi không xác định. Vui lòng liên hệ kỹ thuật để được hỗ trợ!";

	@ExceptionHandler(NotFoundEntityException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorDetail handle(NotFoundEntityException e, WebRequest request) {
		return ErrorDetail.create(e.getErrorField(), e.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(DuplicatedException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public @ResponseBody ErrorDetail handle(DuplicatedException e, WebRequest request) {
		return ErrorDetail.create(e.getErrorField(), e.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public @ResponseBody ErrorDetail handle(AccessDeniedException e, WebRequest request) {
		return ErrorDetail.create(e.getErrorField(), e.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(InputInvalidException.class)
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	public @ResponseBody ErrorDetail handle(InputInvalidException e, WebRequest request) {
		return ErrorDetail.create(e.getErrorField(), e.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorDetail handle(RuntimeException e, WebRequest request) {
		Exception cause = (Exception) e.getCause();
		String error = cause != null ? cause.getMessage() : INTERNAL_ERROR_MESSAGE;
		return ErrorDetail.create(null, error, request.getDescription(false));
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorDetail handle(Exception e, WebRequest request) {
		log.error(e.getMessage(), e);
		return ErrorDetail.create(null, INTERNAL_ERROR_MESSAGE, request.getDescription(false));
	}
}
