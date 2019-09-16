package com.ivnd.sas.mail.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ivnd.sas.mail.entity.User;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 11, 2019
 */

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsernameAndPassword(String username, String password);
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);
}

