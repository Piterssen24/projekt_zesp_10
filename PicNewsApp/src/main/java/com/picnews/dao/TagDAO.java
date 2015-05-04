package com.picnews.dao;

import java.util.List;

import com.picnews.jdbc.model.Tag;

public interface TagDAO {

	//Create
	//dodaje tag do bazy danych
		public void saveTag(Tag tag);
		//Read
		//pobiera tag po id
		public Tag getTagById(int id);
		//Update
		public void update(Tag tag);
		//Delete
		public void deleteTagById(int id);
		//Get All
		//pobiera wszystkie tagi z tabeli category
		public List<Tag> getAllTag();
}