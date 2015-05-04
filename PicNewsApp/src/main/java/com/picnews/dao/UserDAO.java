package com.picnews.dao;

import java.util.List;
import com.picnews.main.*;
import com.picnews.jdbc.model.User;

//CRUD operations
public interface UserDAO {
	
	//Create
	public void saveUser(User user);
	public void saveDevice(String deviceId, int userId);
	//Read
	public User getByName(String name);
	public User getByEmail(String email);
	public User getByToken(String token);
	public User getById(int id);
	//Update
	public void update(User user, String token);
	public void updateUserPhoto(User user, byte[] photo);
	//Delete
	public void deleteById(int id);
	//Get All
	public List<User> getAll();
}
