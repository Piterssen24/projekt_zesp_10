package com.picnews.dao;

import java.util.List;
import com.picnews.main.*;
import com.picnews.jdbc.model.User;

//CRUD operations
public interface UserDAO {
	
	//Create
	public void saveUser(User user);
	//Read
	public User getByName(String name);
	public User getByEmail(String email);
	public User getByToken(String token);
	//Update
	public void update(User user, String token);
	//Delete
	public void deleteById(int id);
	//Get All
	public List<User> getAll();
}
