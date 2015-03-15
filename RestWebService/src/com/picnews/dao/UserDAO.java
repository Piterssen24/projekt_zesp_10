package com.picnews.dao;

import java.util.List;

import com.picnews.jdbc.model.User;

//CRUD operations
public interface UserDAO {
	
	//Create
	public void saveUser(User user);
	//Read
	public User getByName(String name);
	public User getByEmail(String email);
	//Update
	public void update(User employee);
	//Delete
	public void deleteById(int id);
	//Get All
	public List<User> getAll();
}
