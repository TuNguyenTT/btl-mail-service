package com.ivnd.sas.mail.service.Impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivnd.sas.mail.service.MailForGuestService;
import com.ivnd.sas.mail.service.MailService;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 6, 2019
 */

@Service
public class MailForGuestServiceImpl implements MailForGuestService {

	private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
	private @Autowired MailService mailService;

	@Override
	public void sendMail(String email, String activeKey) {
		EXECUTOR.execute(() -> {
			mailService.sendMail(new String[] { email }, new String[0], "Please reset your password",
					"<p>To get a new password reset link, visit: </p>"
					+ "<a href='http://localhost:8080/user/access-account?activeKey=" + activeKey + "'>localhost:8080/user/access-acount?activeKey=" + activeKey + "</a>");
		});
	}

}

