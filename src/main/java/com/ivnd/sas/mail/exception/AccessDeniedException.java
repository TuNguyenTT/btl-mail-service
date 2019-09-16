/***************************************************************************
 * Copyright 2018 by HomeDirect - All rights reserved.                  *    
 **************************************************************************/
package com.ivnd.sas.mail.exception;

import lombok.Getter;

/**
 *  @author: ducdd
 *    Email: duc.do@homedirect.com.vn
 *
 * Apr 4, 2018
 */

public class AccessDeniedException extends Exception{

	private static final long serialVersionUID = -3700782354442444944L;

	@Getter
	private String errorField;

	public AccessDeniedException(String errorField, String message) {
		super(message);
		this.errorField = errorField;
	}
}