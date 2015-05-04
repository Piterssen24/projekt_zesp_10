package com.picnews.dao;

import java.util.List;

import com.picnews.jdbc.model.Faculty;

/*
 * interface odpowiedzialny za operacje na tabeli facuties
 * */
public interface FacultyDAO {
	//Create
	public void saveFaculty(Faculty faculty);
	//Read
	public Faculty getFacultyById(int id);
	//Update
	public void update(Faculty faculty);
	//Delete
	public void deleteFacultyById(int id);
	//Get All
	public List<Faculty> getAllFaculties();
}