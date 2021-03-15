package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration 
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder; 
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().passwordEncoder(passwordEncoder)
			.dataSource(dataSource)
			.usersByUsernameQuery("select email, password, active from form " + " where email=? ")
			.authoritiesByUsernameQuery("select email, role from authority " + " where email=? "); 
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/login").permitAll()
			.antMatchers("/createform").permitAll()
			.antMatchers("/Success/**").hasAnyRole("ADMIN", "USER")
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.failureUrl("/login?error")
			.defaultSuccessUrl("/Success")
			.and()
			.logout().permitAll(); 
	}
	
	@Bean(name="passwordEncoder")
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder(); 
	}
	

}
