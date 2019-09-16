package com.ivnd.sas.mail.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 6, 2019
 */

@Data
public class MailModel {

	@NotEmpty(message = "Email không được để trống")
	@Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message="email không hợp lệ", flags = {Pattern.Flag.CASE_INSENSITIVE})
	private String email;
}

