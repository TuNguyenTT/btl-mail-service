package com.ivnd.sas.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ivnd.sas.mail.service.MailService;
import com.ivnd.sas.mail.service.Impl.MailServiceImpl;

@SpringBootApplication
@Configuration
public class SasMailServiceApplication {

	public static void main( String[] args ) {
		SpringApplication.run(SasMailServiceApplication.class, args);
	}

	@Bean
	public MailService mailService() {
		return new MailServiceImpl();
	}

}
