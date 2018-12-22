package com.rest.practice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.rest.practice.models.User;
import com.rest.practice.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserService userService;
	
	/*
	private static final String[] PUBLIC_MATCHERS = {
			"/css/**", "/js/**", "/image/**", "/menu/**", "/user/**"
	};
	*/
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/menu/**", "/item/**")
				.permitAll()
				
			.and()
			.authorizeRequests()
				.anyRequest().authenticated()
			.and()
			.httpBasic()
			.and()
		    .csrf().disable();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
			.passwordEncoder(User.PASSWORD_ENCODER);
	}
	/*
	//remove later
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/user/**");
	}
	*/
}
