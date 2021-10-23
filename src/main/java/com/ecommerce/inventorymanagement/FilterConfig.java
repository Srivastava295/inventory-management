package com.ecommerce.inventorymanagement;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecommerce.inventorymanagement.filters.AuthenticationFilter;
import com.ecommerce.inventorymanagement.util.ConfigMgrUtil;

@Configuration
public class FilterConfig {

	@Autowired
	ConfigMgrUtil cfg;

	private static final Logger LOG = Logger.getLogger(FilterConfig.class.getName());

	@Bean
	public FilterRegistrationBean<AuthenticationFilter> loggingFilter() {
		FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		LOG.log(Level.INFO, "Registering Authentication filter");
		registrationBean.setFilter(new AuthenticationFilter(cfg));
		
		//Add the URL patterns that need authentication
		registrationBean.addUrlPatterns("/products/*");
		registrationBean.addUrlPatterns("/auth/addToGroup");
		registrationBean.addUrlPatterns("/auth/getUserGroups");

		return registrationBean;

	}
}
