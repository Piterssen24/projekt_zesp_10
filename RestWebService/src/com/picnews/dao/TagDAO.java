package com.picnews.dao;

import java.util.List;

import com.picnews.jdbc.model.Tag;

public interface TagDAO {

	//Create
		public void saveTag(Tag tag);
		//Read
		public Tag getTagById(int id);
		//Update
		public void update(Tag tag);
		//Delete
		public void deleteTagById(int id);
		//Get All
		public List<Tag> getAllTag();
}