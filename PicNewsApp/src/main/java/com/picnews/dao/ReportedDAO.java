package com.picnews.dao;

import java.util.List;

import com.picnews.jdbc.model.Reported;
public interface ReportedDAO {
		//Create
		public void saveReport(int postId, int userId);
		//Read
		public List<Reported> getReportByUserId(int userId);
		
		public List<Reported> getReportByPostId(int postId);
		//Delete
		//public void deleteFacultyById(int id);
		//Get All
		public List<Reported> getAllReported();
}
