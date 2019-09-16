package com.ivnd.sas.mail.controller;

import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ivnd.sas.mail.AppConstants;
import com.ivnd.sas.mail.exception.AccessDeniedException;
import com.ivnd.sas.mail.exception.DuplicatedException;
import com.ivnd.sas.mail.exception.NotFoundEntityException;
import com.ivnd.sas.mail.model.AuthModel;
import com.ivnd.sas.mail.model.ChangePasswordModel;
import com.ivnd.sas.mail.model.GetAccessAccountModel;
import com.ivnd.sas.mail.model.MailModel;
import com.ivnd.sas.mail.model.RegisterModel;
import com.ivnd.sas.mail.model.UserModel;
import com.ivnd.sas.mail.service.MailForGuestService;
import com.ivnd.sas.mail.service.UserService;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 11, 2019
 */

@Controller
@RequestMapping("/user")
public class UserController {

	private Logger log = LoggerFactory.getLogger(getClass());;
	private final UserService service;
	private final MailForGuestService mailService;
	private final ServletContext servletContext;
	private final HttpSession session;

	public UserController(UserService service, MailForGuestService mailService, ServletContext servletContext, HttpSession session) {
		this.service = service;
		this.mailService = mailService;
		this.servletContext = servletContext;
		this.session = session;

	}

	@GetMapping
	public String home(Model model) {
		UserModel userModel = new UserModel();
		model.addAttribute("userModel", userModel);
		return "home";
	}

	@GetMapping("/login")
	public String loginTrantion(Model model) {
		AuthModel  authModel = new AuthModel();
		model.addAttribute("authModel", authModel);
		return "user/login";
	}

	@PostMapping("/login")
	public String exeLogintration(
			@ModelAttribute("authModel") @Valid AuthModel authModel
			, Errors errors
			, Model model) throws NotFoundEntityException, AccessDeniedException {
		if(errors.hasErrors()) {
			return "user/login";
		}
//		try {
			UserModel userModel = service.login(authModel);
			userModel.setPassword(null);
			session.setAttribute(AppConstants.USER_SESSION, userModel);
			log.trace("{}", authModel);
			model.addAttribute("userModel", userModel);
//		} catch (NotFoundEntityException e) {
//			errors.rejectValue(e.getErrorField(), null, e.getMessage());
//		}

		return "home";
	}

	@GetMapping("/register")
	public String inititalRegistration(Model model) {
		RegisterModel  createModel = new RegisterModel();
		model.addAttribute("createModel", createModel);
		return "user/registration";
	}

	@PostMapping("/register")
	public String exeRegistration(@ModelAttribute("createModel") @Valid RegisterModel createModel
			, Errors errors, Model model) throws DuplicatedException, AccessDeniedException {
		if(errors.hasErrors()) {
			return "user/registration";
		}

//		try {
			UserModel userModel = service.register(createModel);
			session.setAttribute(AppConstants.USER_SESSION, userModel);
			model.addAttribute("userModel", userModel);
//		} catch (DuplicatedException e) {
//			errors.rejectValue(e.getErrorField(), null, e.getMessage());
//		} catch (AccessDeniedException e) {
//			errors.rejectValue(e.getErrorField(), null, e.getMessage());
//		}

		return "home";
	}

	@GetMapping("/send-mail")
	public String sendMailTrantion(Model model) {
		MailModel mailModel = new MailModel();
		model.addAttribute("mailModel", mailModel);

		return "user/sendMail";
	}

	@PostMapping("/send-mail")
	public String exeSendMailTrantion(@ModelAttribute("mailModel") @Valid MailModel mailModel, Model model) {
		UUID uuid = UUID.randomUUID();
		String activeKey = uuid.toString();
		String email = mailModel.getEmail();
		servletContext.setAttribute(activeKey, email);
		mailService.sendMail(mailModel.getEmail(), activeKey);
		model.addAttribute("mailModel", mailModel);

		return "user/waiting";
	}

	@GetMapping("/access-account")
	public String getAccessAcountTrantion(@RequestParam("activeKey")String activeKey, Model model) {
		GetAccessAccountModel accessAccountModel = new GetAccessAccountModel();
		String email = (String) servletContext.getAttribute(activeKey);
		accessAccountModel.setEmail(email);
		model.addAttribute("accessAccountModel", accessAccountModel);
		return "user/accessAccount";
	}

	@PostMapping("/access-account")
	public String exeGetAccessAcountTrantion(@ModelAttribute("accessAccountModel") @Valid GetAccessAccountModel accessAccountModel
			, Errors errors, Model model) throws NotFoundEntityException, AccessDeniedException {
		if(errors.hasErrors()) {
			return "user/accessAccount";
		}

		log.info("\n\n\n" + accessAccountModel.getEmail());
//		try {
			UserModel userModel = service.getAccessAccountByEmail(accessAccountModel);
			session.setAttribute(AppConstants.USER_SESSION, userModel);
			model.addAttribute("userModel", userModel);
//		} catch (NotFoundEntityException e) {
//			errors.rejectValue(e.getErrorField(), null, e.getMessage());
//		} catch (AccessDeniedException e) {
//			errors.rejectValue(e.getErrorField(), null, e.getMessage());
//		}

		return "home";
	}

	@GetMapping("/change-password")
	public String changePasswordTrantion(Errors errors,  Model model) {
		ChangePasswordModel changePasswordModel = new ChangePasswordModel();
		model.addAttribute("changePasswordModel", changePasswordModel);
		return "user/changePassword";
	}

	@PostMapping("/change-password")
	public String exeChangePasswordTrantion(@ModelAttribute("changePasswordModel") @Valid ChangePasswordModel changePasswordModel
			, Errors errors, Model model) throws NotFoundEntityException, AccessDeniedException {
		UserModel userModel = (UserModel) session.getAttribute(AppConstants.USER_SESSION);
//		try {
			service.changePassword(userModel.getId(), changePasswordModel);
			model.addAttribute("changePasswordModel", changePasswordModel);
//		} catch (NotFoundEntityException e) {
//			errors.rejectValue(null, null, e.getMessage());
//		} catch (AccessDeniedException e) {
//			errors.rejectValue(null, null, e.getMessage());
//		}

		return "home";

	}
}

