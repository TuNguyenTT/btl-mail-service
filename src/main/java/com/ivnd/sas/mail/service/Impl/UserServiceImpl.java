package com.ivnd.sas.mail.service.Impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ivnd.sas.mail.entity.User;
import com.ivnd.sas.mail.exception.AccessDeniedException;
import com.ivnd.sas.mail.exception.DuplicatedException;
import com.ivnd.sas.mail.exception.NotFoundEntityException;
import com.ivnd.sas.mail.model.AuthModel;
import com.ivnd.sas.mail.model.ChangePasswordModel;
import com.ivnd.sas.mail.model.GetAccessAccountModel;
import com.ivnd.sas.mail.model.RegisterModel;
import com.ivnd.sas.mail.model.UserModel;
import com.ivnd.sas.mail.repository.UserRepository;
import com.ivnd.sas.mail.service.UserService;
import com.ivnd.sas.mail.transformer.UserTransformer;

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
	private final UserTransformer transformer;

	public UserServiceImpl(UserRepository repo, UserTransformer transformer) {
		this.repo = repo;
		this.transformer = transformer;
	}

	@Override
	public UserModel register(RegisterModel model) throws DuplicatedException, AccessDeniedException {
		if (repo.findByUsername(model.getUsername()).isPresent()) {
			throw new DuplicatedException("Tài khoản đã tồn tại");
		}

		validatePassword(model.getPassword());

		if (repo.findByEmail(model.getEmail()).isPresent()) {
			throw new DuplicatedException("Email đã được đăng ký");
		}

		User entity = transformer.toEntity(model);
		entity.setPassword(HashPassword(model.getPassword()));
		repo.save(entity);
		return transformer.toModel(entity);
	}

	@Override
	public UserModel login(AuthModel model) throws NotFoundEntityException, AccessDeniedException {
		User user = loadUserByUsernameOrElseThrow(model);
		validateCurrentPassword(user.getPassword(), HashPassword(model.getPassword()));
		return transformer.toModel(user);
	}

	@Override
	public UserModel changePassword(Long id, ChangePasswordModel model) throws NotFoundEntityException, AccessDeniedException {
		User user = repo.findById(id)
				.orElseThrow(() -> new NotFoundEntityException("id", "Không tìm thấy tài khoản"));
		validateCurrentPassword(user.getPassword(), HashPassword(model.getCurrentPassword()));
		validateNewPassword(model.getNewPassword(), model.getConfirmPassword());
		user.setPassword(HashPassword(model.getConfirmPassword()));
		repo.save(user);
		return transformer.toModel(user);

	}

	@Override
	public UserModel getAccessAccountByEmail(GetAccessAccountModel model) throws NotFoundEntityException, AccessDeniedException {
		User user = repo.findByEmail(model.getEmail())
				.orElseThrow(() -> new NotFoundEntityException("email", "Email không tồn tại"));
		validateNewPassword(model.getNewPassword(), model.getConfirmPassword());
		user.setPassword(HashPassword(model.getConfirmPassword()));
		repo.save(user);
		return transformer.toModel(user);
	}

	private void validateCurrentPassword(String password, String passwordInModel) throws AccessDeniedException {
		if (!password.equals(passwordInModel)) {
			throw new AccessDeniedException("password", "Mật khẩu không khớp");
		}
	}

	private void validateNewPassword(String newPassword, String confirmPassword) throws AccessDeniedException {
		if (!newPassword.equals(confirmPassword)) {
			throw new AccessDeniedException("password", "Mật khẩu không khớp");
		}
	}

	private User loadUserByUsernameOrElseThrow(AuthModel model) throws NotFoundEntityException {
		return repo.findByUsername(model.getUsername())
				.orElseThrow(() -> new NotFoundEntityException("pasword", "Tên đăng nhập hoặc mật khẩu không chính xác"));
	}


    private void validatePassword(String password) throws AccessDeniedException {
    	if (password.length() < 8) {
    		throw new AccessDeniedException("password", "Độ dài mật khẩu ít nhất 8 ký tự");
		}
    	int count = 0;
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
			throw new AccessDeniedException("password", "Mật khẩu phải bao gồm chữ thường, chữ hóa, chữ số và ký tự đặc biệt");
		}
    }

    private boolean matcherPattern(String pattern, String password) {
    	return password.matches(pattern);
    }

    private String HashPassword(String password) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
    }


}

