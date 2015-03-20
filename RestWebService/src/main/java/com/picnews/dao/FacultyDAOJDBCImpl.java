package com.picnews.dao;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.picnews.jdbc.model.Faculty;
import com.picnews.jdbc.model.Tag;

public class FacultyDAOJDBCImpl implements FacultyDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void saveFaculty(Faculty faculty) {
		String query = "insert into faculties (faculty_name, coordinates) values (?,?)";		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
		Object[] args = new Object[] {faculty.getFacultyName(), faculty.getCoordinates()};	
		int out = jdbcTemplate.update(query, args);	
		if(out !=0) {
			System.out.println("Faculty saved with id= " + faculty.getFacultyId());
		} else System.out.println("Faculty save failed with id= " + faculty.getFacultyId());
	}

	@Override
	public Faculty getFacultyById(int id) {
		String query = "select id, faculty_name, coordinates from faculties where id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
		//using RowMapper anonymous class, we can create a separate RowMapper for reuse
		Faculty faculty = jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Faculty>(){
			@Override
			public Faculty mapRow(ResultSet rs, int rowNum) throws SQLException {
				Faculty faculty = new Faculty();
				faculty.setFacultyId(rs.getInt("id"));
				faculty.setFacultyName(rs.getString("faculty_name"));
				faculty.setCoordinates(rs.getString("coordinates"));
				return faculty;
			}});		
		return faculty;
	}

	@Override
	public void update(Faculty faculty) { 
		String query = "update faculties set faculty_name=?, coordinates=? where id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {faculty.getFacultyId(), faculty.getCoordinates()};		
		int out = jdbcTemplate.update(query, args);
		if(out !=0) {
			System.out.println("Faculty updated with id= " + faculty.getFacultyId());
		} else System.out.println("Faculty found with id= " + faculty.getFacultyId()); 
	}

	@Override
	public void deleteFacultyById(int id) {
		String query = "delete from faculties where id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
		int out = jdbcTemplate.update(query, id);
		if(out !=0) {
			System.out.println("Faculty deleted with id= " + id);
		} else System.out.println("No faculty found with id= " + id);
	}

	@Override
	public List<Faculty> getAllFaculties() {
		String query = "select * from faculties";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new RowMapper<Faculty>(){  
			    @Override  
			    public Faculty mapRow(ResultSet rs, int rownumber) throws SQLException {  
			        Faculty faculty = new Faculty();  
			        faculty.setFacultyId(rs.getInt(1));  
			        faculty.setFacultyName(rs.getString(2));
			        faculty.setCoordinates(rs.getString(3));
			        return faculty;  
			    }  
		});
	}
}


