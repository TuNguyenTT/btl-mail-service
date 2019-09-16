package com.ivnd.sas.mail.exception;

import lombok.Getter;

/**
 *  @author: ducdd
 *    Email: duc.do@homedirect.com.vn
 *
 * Feb 28, 2019
 */

public class DuplicatedException extends Exception {

	private static final long serialVersionUID = -6129951984467636499L;

	@Getter
	private String errorField;

	public DuplicatedException(String errorField, String message) {
		super(message);
		this.errorField = errorField;
	}
	
	public DuplicatedException(String message) {
		super(message);
	}
}
