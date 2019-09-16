package com.ivnd.sas.mail.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ivnd.sas.mail.model.ErrorDetail;

import lombok.extern.slf4j.Slf4j;

/**
 * Author : duybv
 * Feb 24, 2019
 */

@ControllerAdvice
@RestController
@Slf4j
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return toResponse(ex.getBindingResult(), request);

	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return toResponse(ex.getBindingResult(), request);
	}

	private ResponseEntity<Object> toResponse(BindingResult bindingResult, WebRequest webRequest) {
		String message = bindingResult.getFieldError().getDefaultMessage();
		String field = bindingResult.getFieldError().getField();
		log.warn("Message: {}, Field: {}", message, field);
		return new ResponseEntity<>(ErrorDetail.create(field, message, webRequest.getDescription(false)), HttpStatus.BAD_REQUEST);
	}
}
