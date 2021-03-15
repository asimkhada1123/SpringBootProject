package com.example.demo;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

	
	@Autowired 
	 private UserValidator userValidator; 
	
	@Autowired
	private PasswordEncoder passwordEncoder; 
	
	@Autowired
	UserDao userDao; 
	//UserDao userDao; 
	
	@GetMapping("/login")
	public String showHome() {
		return "login"; 
	}
	
//	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	public String createLogin(Model model, BindingResult result) {
//		
//		if(result.hasErrors()) {
//			return "login"; 
//		}
//		return "Success"; 
//	}
	
	@GetMapping("/createform")
	public String createForm(User user, Model model,Principal principal) {
		
		model.addAttribute("user", new User()); 
		model.addAttribute("genders", getAllTypes()); 
		
		return "createform"; 
	}
	
	private ArrayList<String> getAllTypes() {
		ArrayList<String> list = new ArrayList<>();
		
		list.add("Male"); 
		list.add("Female");
		list.add("Don't want to identify");
		
		return list; 
	}

	@RequestMapping(value = "/createform", method = RequestMethod.POST)
	public String createForm(@Valid User user, BindingResult result,Model model) {
		
		userValidator.validate(user, result);
		if(result.hasErrors()) {
			model.addAttribute("genders", getAllTypes()); 
			return "createform"; 
		}
		
		//Encoding the password that we get
		String encodedPswd = passwordEncoder.encode(user.getPassword()); 
		user.setPassword(encodedPswd);
		//Determining the roles of the given user
		List<String> roles = determineRoles(user); 
		//Need to Update UserDao so that it  takes roles 
		
		
		userDao.addUser(user,roles); 
		return "success"; 
	}
	
	
	
	private List<String> determineRoles(User user){
		
		ArrayList<String> roles = new ArrayList<>(); 
		
		if(user.getGender().equals("Male") || user.getGender().equals("Female")){
			roles.add("ADMIN"); 
			roles.add("USER"); 
		}
		else {
			roles.add("NOTALLOWED"); 
		}
	
		return roles;
	}
	
	
	
	@GetMapping("/signin")
	public String signIn(Model model) {
		model.addAttribute("user",new User()); 
		
		return "signin"; 
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public String signIn(@Valid User user, BindingResult result) {
		
		userValidator.validate(user, result);
		
		if(result.hasErrors()) {
			System.out.println("There are errors ");
			return "signin"; 
		}
		
		return "success"; 	
	}
	
	@Bean(name="passwordEncoder1")
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder(); 
	}

}
