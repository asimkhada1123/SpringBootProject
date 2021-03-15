package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Repository
public class UserDaoImpl implements UserDao{
	
	@Autowired
	private DataSourceTransactionManager transactionManager; 
	
	private DataSource dataSource; 
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource; 
		this.jdbcTemplate = new JdbcTemplate(this.dataSource); 
	}
	
	@Override
	public User addUser(User user, List<String> roles) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def); 
		
		try {
			String SQL = "insert into form (name, email, password, confpassword, gender) " + " values (?, ?, ?, ?, ?)"; 
			
			jdbcTemplate.update(SQL, user.getName(), user.getEmail(), user.getPassword()
					, user.getConfPassword(), user.getGender()); 
			
			SQL = "insert into authority (email, role) " + " values (?, ?)"; 
			
			for(String role: roles) {
				jdbcTemplate.update(SQL, user.getEmail(), role); 
			}
			
			
			transactionManager.commit(status);
		//Catching the DataAccessException
		} catch(DataAccessException e) {
			
			System.out.println("There is an error here"); 
			transactionManager.rollback(status);
			throw e; 
			
		}
		return user; 
	}
	
	@Override
	public User findUserByEmail(String email) {
		String SQL = "select * from form where email = ?"; 
	
		//Very tori logic 
		try {
			User user = jdbcTemplate.queryForObject(SQL, new UserMapper(), email);
			System.out.println("+++++++++++"); 
			return user; 
		} catch(Exception E)
		{
			return null; 
		}
	
	}
	
	
	class UserMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setConfPassword(rs.getString("confpassword"));
			user.setEmail(rs.getString("email"));
			user.setGender(rs.getString("gender"));
			return user; 
			
		}
		
	}
	
	
}
