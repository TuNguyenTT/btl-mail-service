package com.ivnd.sas.mail.service.Impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.ivnd.sas.mail.entity.User;
import com.ivnd.sas.mail.exception.AccessDeniedException;
import com.ivnd.sas.mail.exception.NotFoundEntityException;
import com.ivnd.sas.mail.model.AuthModel;
import com.ivnd.sas.mail.model.ChangePasswordModel;
import com.ivnd.sas.mail.model.GetAccessAccountModel;
import com.ivnd.sas.mail.repository.UserRepository;
import com.ivnd.sas.mail.service.UserService;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 11, 2019
 */

@Service
public class UserServiceImpl implements UserService {

	private final String LOWERCASE_PATTERN = ".*[a-z].*";
	private final String UPPERCASE_PATTERN = ".*[A-Z].*";
	private final String NUMBER_PATTERN = ".*\\d.*";
	private final String SPECIAL_PATTERN = ".*[@#$%].*";

	private final UserRepository repo;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserServiceImpl(UserRepository repo, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.repo = repo;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

//	@Override
//	public User register(User model) throws DuplicatedException, AccessDeniedException, NoSuchAlgorithmException {
//		if (repo.findByUsername(model.getUsername()).isPresent()) {
//			throw new DuplicatedException("Tài khoản đã tồn tại");
//		}
//
//		validatePassword(model.getPassword());
//
//		if (repo.findByEmail(model.getEmail()).isPresent()) {
//			throw new DuplicatedException("Email đã được đăng ký");
//		}
//
//		User entity = transformer.toEntity(user);
//		entity.setPassword(HashPassword(model.getPassword()));
//		repo.save(entity);
//		return entity;
//	}

//	@Override
//	public User login(AuthModel model) throws NotFoundEntityException, AccessDeniedException, NoSuchAlgorithmException {
//		User user = loadUserByUsernameOrElseThrow(model);
//		validateCurrentPassword(user.getPassword(), HashPassword(model.getPassword()));
//		return user;
//	}

	@Override
	public User changePassword(Long id, ChangePasswordModel model) throws NotFoundEntityException, AccessDeniedException, NoSuchAlgorithmException {
		User user = repo.findById(id)
				.orElseThrow(() -> new NotFoundEntityException("id", "Không tìm thấy tài khoản"));
		validateCurrentPassword(user.getPassword(), HashPassword(model.getCurrentPassword()));
		validateNewPassword(model.getNewPassword(), model.getConfirmPassword());
		user.setPassword(HashPassword(model.getConfirmPassword()));
		repo.save(user);
		return user;

	}

	@Override
	public User getAccessAccountByEmail(GetAccessAccountModel model) throws NotFoundEntityException, AccessDeniedException, NoSuchAlgorithmException {
		User user = repo.findByEmail(model.getEmail())
				.orElseThrow(() -> new NotFoundEntityException("email", "Email không tồn tại"));
		validateNewPassword(model.getNewPassword(), model.getConfirmPassword());
		user.setPassword(HashPassword(model.getConfirmPassword()));
		repo.save(user);
		return user;
	}

	private void validateCurrentPassword(String password, String passwordInModel) throws AccessDeniedException {
		System.out.println(password + "\n" + passwordInModel);
		if (!password.equals(passwordInModel)) {
			throw new AccessDeniedException("password", "Mật khẩu không khớp");
		}
	}

	private void validateNewPassword(String newPassword, String confirmPassword) throws AccessDeniedException {
		if (!newPassword.equals(confirmPassword)) {
			throw new AccessDeniedException("password", "Mật khẩu không khớp");
		}
	}

//	private User loadUserByUsernameOrElseThrow(User user) throws NotFoundEntityException {
//		return repo.findByUsername(user.getUsername())
//	}


	private void validatePassword(String password, BindingResult bindingResult) {
		int count = 0;
		if (password.length() < 8) {
			bindingResult
			.rejectValue("password", "error.user", "Mật khẩu có độ dài tối thiểu 8 ký tự");
		}
		if (matcherPattern(LOWERCASE_PATTERN, password)) {
			count++;
		}

		if (matcherPattern(UPPERCASE_PATTERN, password)) {
			count++;
		}

		if (matcherPattern(NUMBER_PATTERN, password)) {
			count++;
		}

		if (matcherPattern(SPECIAL_PATTERN, password)) {
			count++;
		}

		if (count < 2) {
			bindingResult
			.rejectValue("password", "error.user", "Mật khẩu phải có 2 trong các nhóm: chữ hoa. chữ thường, chữ số và ký tự đặ biêt!");
		}
	}

	private boolean matcherPattern(String pattern, String password) {
		return password.matches(pattern);
	}

	private String HashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] messageDigest = md.digest(password.getBytes());
		return convertByteToHex(messageDigest);
	}

	@Override
	public User get(Long id) throws NotFoundEntityException {
		return repo.findById(id)
				.orElseThrow(() -> new NotFoundEntityException("id", "Không tìm thấy thông tin tài khoản"));
	}

	private String convertByteToHex(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	@Override
	public User findByEmail(String email) {
		return repo.findByEmail(email).get();
	}

	@Override
	public void register(User user, BindingResult bindingResult, ModelAndView modelAndView) {
		if (repo.findByUsername(user.getUsername()).isPresent()) {
			bindingResult
			.rejectValue("email", "error.user", "Tài khoản đã tồn tại");
		}
		if(repo.findByEmail(user.getEmail()).isPresent()) {
			bindingResult
			.rejectValue("email", "error.user", "Email đã được đăng ký");
		}

		validatePassword(user.getPassword(), bindingResult);

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
		}
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		repo.save(user);
	}

	@Override
	public void login(@Valid User user, BindingResult bindingResult) {
		User entity = repo.findByUsername(user.getUsername()).get();
		if (entity == null || !entity.getPassword().equals(bCryptPasswordEncoder.encode(user.getPassword()))) {
			bindingResult
			.rejectValue("username", "error.user", "Tài khoản hoặc mật khẩu không chính xác");
		}
	}
}

