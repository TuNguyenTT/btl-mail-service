package com.ivnd.sas.mail.model;

import lombok.Getter;

/**
 *  @author: ducdd
 *    Email: duc.do@homedirect.com.vn
 *
 * Feb 28, 2019
 */

@Getter
public class ErrorDetail {

	private String errorField;
	private String error;
	private String message;
	
	private ErrorDetail(String errorField, String error, String message) {
		this.errorField = errorField;
		this.error = error;
		this.message = message;
	}
	
	public static ErrorDetail create(String errorField, String error, String message) {
		return new ErrorDetail(errorField, error, message);
	}

}
