package com.ivnd.sas.mail.service.Impl;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.ivnd.sas.mail.service.MailService;

/**
 * @author: Nguyen The Tu
 *   email: tu.nguyenthe@ivnd.com.vn
 *
 * @date: Sep 6, 2019
 */

public class MailServiceImpl implements MailService, DisposableBean {

	@Autowired
	private JavaMailSender mailSender;

	private final Logger LOGGER;
	private final ExecutorService EXECUTOR;

	public MailServiceImpl() {
		LOGGER = LoggerFactory.getLogger(getClass());

		EXECUTOR = Executors.newFixedThreadPool(5);
	}

	@Override
	public void destroy() throws Exception {
		EXECUTOR.shutdown();
	}

	public void sendMail(String[] emailAddress, String[] cc, String subject, String message) {
		EXECUTOR.execute(() -> {
			try {
				InternetAddress[] emailAddresss = new InternetAddress[emailAddress.length];
				for (int j = 0; j < emailAddress.length; j++) {
					emailAddresss[j] = new InternetAddress(emailAddress[j]);
				}

				InternetAddress[] ccAddresses = new InternetAddress[cc.length];
				for (int j = 0; j < cc.length; j++) {
					ccAddresses[j] = new InternetAddress(cc[j]);
				}

				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
				mimeMessageHelper.setTo(emailAddresss);
				mimeMessageHelper.setCc(ccAddresses);
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setText(message, true);
				/*mimeMessage.setRecipients(Message.RecipientType.TO, emailAddresss);
				mimeMessage.setRecipients(Message.RecipientType.BCC, ccAddresses);
				mimeMessage.setSubject(subject, "utf-8");
				mimeMessage.setContent(message, "text/html");*/

				MailcapCommandMap mailcapCommandMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
				mailcapCommandMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mailcapCommandMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mailcapCommandMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mailcapCommandMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mailcapCommandMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mailcapCommandMap);

				mailSender.send(mimeMessage);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		});
	}
}
