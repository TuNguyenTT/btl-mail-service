package com.ivnd.sas.mail.service;

import com.ivnd.sas.mail.exception.AccessDeniedException;
import com.ivnd.sas.mail.exception.DuplicatedException;
import com.ivnd.sas.mail.exception.NotFoundEntityException;
import com.ivnd.sas.mail.model.AuthModel;
import com.ivnd.sas.mail.model.ChangePasswordModel;
import com.ivnd.sas.mail.model.GetAccessAccountModel;
import com.ivnd.sas.mail.model.RegisterModel;
import com.ivnd.sas.mail.model.UserModel;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 11, 2019
 */

public interface UserService {

	UserModel register(RegisterModel model) throws DuplicatedException, AccessDeniedException;

	UserModel login(AuthModel model) throws NotFoundEntityException, AccessDeniedException;

	UserModel getAccessAccountByEmail(GetAccessAccountModel model) throws NotFoundEntityException, AccessDeniedException;

	UserModel changePassword(Long id, ChangePasswordModel model) throws NotFoundEntityException, AccessDeniedException;
}

