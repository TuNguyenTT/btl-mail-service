package com.ivnd.sas.mail.service;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 6, 2019
 */

public interface MailForGuestService {

	void sendMail(String email, String activeKey);
}

