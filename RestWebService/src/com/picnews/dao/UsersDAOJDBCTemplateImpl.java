package com.picnews.dao;
import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.picnews.jdbc.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UsersDAOJDBCTemplateImpl implements UserDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	@Transactional
	public void saveUser(User user) {
		String query = "insert into users (login, pass, email, role) values (?,?,?,?)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		Object[] args = new Object[] {user.getLogin(), user.getPass(), user.getEmail(), user.getRole()};

		int out = jdbcTemplate.update(query, args);
		
		if(out !=0){
			System.out.println("User saved with id= " + user.getId());
		}else System.out.println("User save failed with id= " + user.getId());
	}

	@Override
	public User getByName(String name) {
		String query = "select id, login, pass, email, role from users where login = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
		//using RowMapper anonymous class, we can create a separate RowMapper for reuse
		User user = jdbcTemplate.queryForObject(query, new Object[]{name}, new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum)
					throws SQLException {
						User user = new User();
						user.setId(rs.getInt("id"));
						user.setLogin(rs.getString("login"));
						user.setPass(rs.getString("pass"));
						user.setEmail(rs.getString("email"));
						user.setRole(rs.getString("role"));
				return user;
			}});		
		return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	
	@Override
	public User getByEmail(String email) {
		String query = "select id, login, pass, email, role from users where email = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
		//using RowMapper anonymous class, we can create a separate RowMapper for reuse
		User user = jdbcTemplate.queryForObject(query, new Object[]{email}, new RowMapper<User>(){

			@Override
			public User mapRow(ResultSet rs, int rowNum)
					throws SQLException {
						User user = new User();
						user.setId(rs.getInt("id"));
						user.setLogin(rs.getString("login"));
						user.setPass(rs.getString("pass"));
						user.setEmail(rs.getString("email"));
						user.setRole(rs.getString("role"));
				return user;
			}});		
		return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void update(User user) {
		String query = "update users set login=?, pass=?, role=? where id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {user.getLogin(), user.getPass(), user.getRole(), user.getId()};
		
		int out = jdbcTemplate.update(query, args);
		if(out !=0){
			System.out.println("User updated with id= " + user.getId());
		}else System.out.println("User found with id= " + user.getId());
	}

	@Override
	public void deleteById(int id) {

		String query = "delete from users where id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		int out = jdbcTemplate.update(query, id);
		if(out !=0){
			System.out.println("User deleted with id= " + id);
		}else System.out.println("No user found with id= " + id);
	}

	@Override
	public List<User> getAll() {
		String query = "select id, login, pass, role from users";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<User> empList = new ArrayList<User>();

		List<Map<String,Object>> userRows = jdbcTemplate.queryForList(query);
		
		for(Map<String,Object> userRow : userRows){
			User user = new User();
			user.setId(Integer.parseInt(String.valueOf(userRow.get("id"))));
			user.setLogin(String.valueOf(userRow.get("login")));
			user.setPass(String.valueOf(userRow.get("pass")));
			user.setRole(String.valueOf(userRow.get("role")));
			empList.add(user);
		}
		return empList;
	  
	}

}
