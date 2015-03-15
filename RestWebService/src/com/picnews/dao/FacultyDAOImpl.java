package com.picnews.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.picnews.jdbc.model.Faculty;
import com.picnews.jdbc.model.Tag;

public class FacultyDAOImpl implements FacultyDAO{
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void saveFaculty(Faculty faculty) {
		String query = "insert into faculties (faculty_name, coordinates) values (?,?)";
		PreparedStatement ps = null;
		Connection con = null;		
		try {		
			con = dataSource.getConnection();		
			ps = con.prepareStatement(query);
			ps.setString(1, faculty.getFacultyName());
			ps.setString(2, faculty.getCoordinates());								
			int out = ps.executeUpdate();		
			if(out !=0){
				System.out.println("Faculty saved with id= " + faculty.getFacultyId());
			} else System.out.println("Faculty save failed with id= " + faculty.getFacultyId());
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Faculty getFacultyById(int id) {
		String query = "select id, faculty_name, coordinates from faculties where id = ?";
		Faculty faculty = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
			    faculty = new Faculty();
				faculty.setFacultyId(rs.getInt("id"));
				faculty.setFacultyName(rs.getString("faculty_name"));
				faculty.setCoordinates(rs.getString("coordinates"));
				System.out.println("Faculty found:: " + faculty);
			}else{
				System.out.println("Faculty found with id= " + faculty.getFacultyId());
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return faculty;
	}

	@Override
	public void update(Faculty faculty) {
		String query = "update faculties set faculty_name=?, coordinates=? where id=?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, faculty.getFacultyName());
			ps.setString(2, faculty.getCoordinates());
			ps.setInt(3, faculty.getFacultyId());
			int out = ps.executeUpdate();
			if(out !=0){
				System.out.println("Faculty updated with id=" + faculty.getFacultyId());
			} else System.out.println("No faculty found with id=" + faculty.getFacultyId());
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteFacultyById(int id) {
		String query = "delete from faculties where id=?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			int out = ps.executeUpdate();
			if(out !=0){
				System.out.println("Faculty deleted ");
			} else System.out.println("Faculty found ");
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Faculty> getAllFaculties() {
		String query = "select * from faculties";
		List<Faculty> facultyList = new ArrayList<Faculty>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				Faculty faculty = new Faculty();
				faculty.setFacultyId(rs.getInt("id"));
				faculty.setFacultyName(rs.getString("faculty_name"));
				faculty.setCoordinates(rs.getString("coordinates"));
				facultyList.add(faculty);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return facultyList;
	}
}

