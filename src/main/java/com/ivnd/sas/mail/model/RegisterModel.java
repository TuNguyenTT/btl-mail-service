package com.ivnd.sas.mail.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterModel {

	@NotEmpty(message = "Tên đăng nhập không được để trống")
	private String username;

	@NotEmpty(message = "Mật khẩu không được để trống")
	private String password;

	@NotEmpty(message = "Email không được để trống")
	@Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message="email không hợp lệ", flags = {Pattern.Flag.CASE_INSENSITIVE})
	private String email;
}
