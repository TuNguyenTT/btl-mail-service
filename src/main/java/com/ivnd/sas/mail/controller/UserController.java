package com.ivnd.sas.mail.controller;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ivnd.sas.mail.AppConstants;
import com.ivnd.sas.mail.entity.User;
import com.ivnd.sas.mail.exception.AccessDeniedException;
import com.ivnd.sas.mail.exception.NotFoundEntityException;
import com.ivnd.sas.mail.model.ChangePasswordModel;
import com.ivnd.sas.mail.model.GetAccessAccountModel;
import com.ivnd.sas.mail.model.MailModel;
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
	public ModelAndView loginTrantion() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@PostMapping("/login")
	public ModelAndView login(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		service.login(user, bindingResult);
		
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("login");
		} else {
			modelAndView.addObject("successMessage", "User has been login successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("login");

		}
		return modelAndView;
	}

	@GetMapping("/registration")
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@PostMapping("/registration")
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		service.register(user, bindingResult, modelAndView);

		return modelAndView;
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
			, Errors errors, Model model) throws NotFoundEntityException, AccessDeniedException, NoSuchAlgorithmException {
		if(errors.hasErrors()) {
			return "user/accessAccount";
		}

		User user = service.getAccessAccountByEmail(accessAccountModel);
		session.setAttribute(AppConstants.USER_SESSION, user);
		model.addAttribute("user", user);

		return "home";
	}

	@GetMapping("/change-password/{id}")
	public String changePasswordTrantion(@PathVariable("id") Long id, Model model) throws NotFoundEntityException {
		User user = service.get(id);
		ChangePasswordModel changePasswordModel = new ChangePasswordModel();
		changePasswordModel.setId(user.getId());
		model.addAttribute("changePasswordModel", changePasswordModel);
		return "user/changePassword";
	}

	@PostMapping("/change-password/{id}")
	public String exeChangePasswordTrantion(@PathVariable("id") Long id, @ModelAttribute("changePasswordModel") @Valid ChangePasswordModel changePasswordModel
			, Errors errors, Model model) throws NotFoundEntityException, AccessDeniedException, NoSuchAlgorithmException {

		User user = service.changePassword(id, changePasswordModel);
		model.addAttribute("user", user);

		return "home";

	}
}

