package com.picnews.dao;

import java.util.List;
import com.picnews.main.*;
import com.picnews.jdbc.model.Followed;

//CRUD operations
public interface FollowedDAO {
	
	//Create
	public void saveFollow(int userId, int followedUserId);
	//Read
	public List<Followed> getByUserId(int userId);
	//Delete
	public void deleteByUserId(int userId, int followedUserId);
}