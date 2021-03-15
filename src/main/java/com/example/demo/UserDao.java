package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

	//public Website createUser(User); 
	public User addUser(User user, List<String>roles); 
	public User findUserByEmail(String email);
}
