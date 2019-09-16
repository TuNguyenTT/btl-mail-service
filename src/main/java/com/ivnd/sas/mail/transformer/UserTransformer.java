package com.ivnd.sas.mail.transformer;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.ivnd.sas.mail.entity.User;
import com.ivnd.sas.mail.model.RegisterModel;
import com.ivnd.sas.mail.model.UserModel;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 11, 2019
 */

@Component
public class UserTransformer {

	public User toEntity(RegisterModel model) {
		User entity = new User();

		entity.setUsername(model.getUsername());
		entity.setEmail(model.getEmail());
		entity.setCreated(new Date());

		return entity;
	}

	public UserModel toModel(User entity) {
		UserModel model = new UserModel();

		model.setId(entity.getId());
		model.setUsername(entity.getUsername());
		model.setPassword(entity.getPassword());
		model.setEmail(entity.getEmail());
		model.setCreated(entity.getCreated() == null ? null : entity.getCreated().getTime());

		return model;
	}
}

