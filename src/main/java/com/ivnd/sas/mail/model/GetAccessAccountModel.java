package com.ivnd.sas.mail.model;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetAccessAccountModel {

	@NotEmpty(message = "Email không được để trống")
	private String email;

	@NotEmpty(message = "Mật khẩu không được để trống")
	private String newPassword;

	@NotEmpty(message = "Mật khẩu không được để trống")
	private String confirmPassword;
}
