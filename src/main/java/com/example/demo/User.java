package com.example.demo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import javax.validation.constraints.Email;

public class User {

	
	@NotEmpty
	private String Name; 
	
	@NotEmpty
	@Email
	private String Email; 
	
	@Size(min=3,max=15)
	private String Password; 
	
	@Size(min=3,max=15)
	private String ConfPassword;
	
	@NotEmpty
	private String Gender;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getConfPassword() {
		return ConfPassword;
	}

	public void setConfPassword(String confPassword) {
		ConfPassword = confPassword;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	} 
	
  
	
	
}
