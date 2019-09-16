package com.ivnd.sas.mail.model;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangePasswordModel {

	private Long id;

	@NotEmpty(message = "Mật khẩu không được để trống")
	private String currentPassword;

	@NotEmpty(message = "Mật khẩu không được để trống")
	private String newPassword;

	@NotEmpty(message = "Mật khẩu không được để trống")
	private String confirmPassword;
}
