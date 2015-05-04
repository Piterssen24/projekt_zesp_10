package com.picnews.dao;

import java.util.List;

import com.picnews.jdbc.model.Post;

public interface PostDAO {
		
	//Create
	public void savePost(Post post) ;
	//Read
	public List<Post> getPostById(int id);
	public List<Post> getPostByCategoryId(int id, int tag);
	public List<Post> getPostByFavIds(Object[] favCategoryId);
	public List<Post> getPostByFolIds(Object[] folUserId);
	public List<Post> getNearlyPosts(int id);
	public int getMaxId();
	//Update
	public void update(Post post);
	public void reportPostById(int id);
	public void updatePhoto(int id, String data);
	//Delete
	public void deletePostById(int id);
	//Get All
	public List<Post> getAllPost();
	
	public List<Post> getPostByUserId(int id);
	
}
