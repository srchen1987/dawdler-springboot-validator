package com.anywide.springboot.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;

/**
 * @author jackson.song
 * @version V1.0
 * @Title FilterConfig.java
 * @Description springboot注入filter配置
 * @date 2019年11月21日
 * @email suxuan696@gmail.com
 */
@Configuration
public class ValidateFilterConfig {

	@Bean("validateFilterFilterRegistrationBean")
	public FilterRegistrationBean<Filter> filterRegistration() {
		ValidateFilter validateFilter = new ValidateFilter();
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>(validateFilter);
		registration.addUrlPatterns("/*");
		registration.setOrder(1);
		registration.setBeanName("validateFilterRegistrationBean");
		return registration;
	}
}