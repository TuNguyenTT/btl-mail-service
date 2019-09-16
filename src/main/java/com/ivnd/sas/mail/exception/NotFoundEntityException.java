package com.ivnd.sas.mail.exception;

import lombok.Getter;

/**
 *  @author: ducdd
 *    Email: duc.do@homedirect.com.vn
 *
 * Feb 28, 2019
 */

public class NotFoundEntityException extends Exception {

	private static final long serialVersionUID = -718639735490655218L;

	@Getter
	private String errorField;

	public NotFoundEntityException(String errorField, String message) {
		super(message);
		this.errorField = errorField;
	}
}
