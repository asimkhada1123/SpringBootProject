package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator{
	
	//@Autowired
	User user; 

	@Autowired
	UserDao userDao; 
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz); 
	}
	
	public void validate(Object target, Errors errors) {
		User user = (User)target; 
		
		String password = user.getPassword(); 
		String confPassword = user.getConfPassword(); 
		
		if(!password.equals(confPassword)) {
			
			System.out.println("Hey"); 
			//This Password should be the same as password 
			errors.rejectValue("Password", "user.password.misMatch");
			errors.rejectValue("ConfPassword", "user.password.misMatch"); 
		}
		
		String email = user.getEmail(); 
		
		System.out.println(email); 
	
		//user = userDao.findUserByEmail(email); 
		//Very tori logic 
		if(userDao.findUserByEmail(email) != null) {
			errors.rejectValue("Email", "user.email.misMatch");
		}

	}
}
