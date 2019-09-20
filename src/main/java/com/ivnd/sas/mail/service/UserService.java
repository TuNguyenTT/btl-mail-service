package com.ivnd.sas.mail.service;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.ivnd.sas.mail.entity.User;
import com.ivnd.sas.mail.exception.AccessDeniedException;
import com.ivnd.sas.mail.exception.NotFoundEntityException;
import com.ivnd.sas.mail.model.AuthModel;
import com.ivnd.sas.mail.model.ChangePasswordModel;
import com.ivnd.sas.mail.model.GetAccessAccountModel;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 11, 2019
 */

public interface UserService {

	User get(Long id) throws NotFoundEntityException;

	void login(@Valid User user, BindingResult bindingResult);

	void register(User user, BindingResult bindingResult, ModelAndView modelAndView);

	User getAccessAccountByEmail(GetAccessAccountModel model) throws NotFoundEntityException, AccessDeniedException, NoSuchAlgorithmException;

	User changePassword(Long id, ChangePasswordModel model) throws NotFoundEntityException, AccessDeniedException, NoSuchAlgorithmException;

	User findByEmail(String email);


}

