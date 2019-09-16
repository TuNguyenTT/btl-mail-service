package com.ivnd.sas.mail.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 11, 2019
 */

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel {
	
	private Long id;
	private String username;
	private String password;
	private String email;
	private Long created;
}

