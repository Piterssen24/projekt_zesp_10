package com.picnews.dao;

import java.sql.SQLException;
import java.util.List;

import com.picnews.jdbc.model.Post;

public interface PostDAO {
		
	//Create
	public void savePost(Post post) ;
	//Read
	public Post getPostById(int id);
	//Update
	public void update(Post post);
	//Delete
	public void deletePostById(int id);
	//Get All
	public List<Post> getAllPost();
	
}
