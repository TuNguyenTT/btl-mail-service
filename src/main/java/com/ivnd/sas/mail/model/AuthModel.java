package com.ivnd.sas.mail.model;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 11, 2019
 */

@Getter @Setter
public class AuthModel {

	@NotEmpty(message = "Tên đăng nhập không được để trống")
	private String username;

	@NotEmpty(message = "Mật khẩu không được để trống")
	private String password;
}

