package com.ivnd.sas.mail.exception;

import lombok.Getter;

/**
 *  @author: ducdd
 *    Email: duc.do@homedirect.com.vn
 *
 * May 15, 2019
 */

public class InputInvalidException extends Exception {

	private static final long serialVersionUID = 3040416053638688121L;

	@Getter
	private String errorField;

	public InputInvalidException(String errorField, String message) {
		super(message);
		this.errorField = errorField;
	}
}
