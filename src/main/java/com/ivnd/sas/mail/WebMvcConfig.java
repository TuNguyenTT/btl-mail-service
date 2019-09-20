package com.ivnd.sas.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author : duybv Feb 24, 2019
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.ivnd.sas.mail.controller.impl")
public class WebMvcConfig implements WebMvcConfigurer {

//	@Autowired
//	private ApplicationContext applicationContext;
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

	   /*
	    * STEP 1 - Create SpringResourceTemplateResolver
	    * */
//	   @Bean
//	   public SpringResourceTemplateResolver templateResolver() {
//	      SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//	      templateResolver.setApplicationContext(applicationContext);
//	      templateResolver.setPrefix("/WEB-INF/views/");
//	      templateResolver.setSuffix(".html");
//	      return templateResolver;
//	   }

	   /*
	    * STEP 2 - Create SpringTemplateEngine
	    * */
//	   @Bean
//	   public SpringTemplateEngine templateEngine() {
//	      SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//	      templateEngine.setTemplateResolver(templateResolver());
//	      templateEngine.setEnableSpringELCompiler(true);
//	      return templateEngine;
//	   }

	   /*
	    * STEP 3 - Register ThymeleafViewResolver
	    * */
//	   @Override
//	   public void configureViewResolvers(ViewResolverRegistry registry) {
//	      ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//	      resolver.setTemplateEngine(templateEngine());
//	      registry.viewResolver(resolver);
//	   }
}
