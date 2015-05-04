package com.picnews.dao;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.picnews.jdbc.model.Faculty;
import com.picnews.jdbc.model.Reported;


public class ReportedDAOJDBCImpl implements ReportedDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void saveReport(int postId, int userId) {
		String query = "insert into reported (post_id, user_id) values (?,?)";		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
		Object[] args = new Object[] {postId, userId};	
		int out = jdbcTemplate.update(query, args);	
		//if(out !=0) {
		//	System.out.println("Faculty saved with id= " + faculty.getFacultyId());
		//} else System.out.println("Faculty save failed with id= " + faculty.getFacultyId());
	}

	@Override
	public List<Reported> getReportByUserId(int id) {
		String query = "select post_id, user_id from reported where user_id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new Object[]{id}, new RowMapper<Reported>(){  
			    @Override  
			    public Reported mapRow(ResultSet rs, int rownumber) throws SQLException {  
			    	Reported report = new Reported();
					report.setUserId(rs.getInt("user_id"));
					report.setPostId(rs.getInt("post_id"));
					return report;
			    }  
		});
	}

	@Override
	public List<Reported> getReportByPostId(int id) {
		String query = "select post_id, user_id from reported where post_id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new Object[]{id}, new RowMapper<Reported>(){  
			    @Override  
			    public Reported mapRow(ResultSet rs, int rownumber) throws SQLException {  
			    	Reported report = new Reported();
					report.setUserId(rs.getInt("user_id"));
					report.setPostId(rs.getInt("post_id"));
					return report;
			    }  
		});
	}
/*
	@Override
	public void deleteFacultyById(int id) {
		String query = "delete from faculties where id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
		int out = jdbcTemplate.update(query, id);
		if(out !=0) {
			System.out.println("Faculty deleted with id= " + id);
		} else System.out.println("No faculty found with id= " + id);
	}
*/
	@Override
	public List<Reported> getAllReported() {
		String query = "select * from reported";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new RowMapper<Reported>(){  
			    @Override  
			    public Reported mapRow(ResultSet rs, int rownumber) throws SQLException {  
			    	Reported report = new Reported();
					report.setUserId(rs.getInt("user_id"));
					report.setPostId(rs.getInt("post_id"));
					return report;
			    }  
		});
	}
}
