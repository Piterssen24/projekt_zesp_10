package com.picnews.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.picnews.jdbc.model.Tag;

public class TagDAOImpl implements TagDAO{
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void saveTag(Tag tag) {
		String query = "insert into category (tag) values (?)";
		PreparedStatement ps = null;
		Connection con = null;
		try {		
			con = dataSource.getConnection();		
			ps = con.prepareStatement(query);
			ps.setString(1, tag.getTag());								
			int out = ps.executeUpdate();		
			if(out !=0) {
				System.out.println("Tag saved with id= " + tag.getCategoryId());
			} else System.out.println("Tag save failed with id= " + tag.getCategoryId());
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
	public Tag getTagById(int id) {
		String query = "select category_id, tag from category where category_id = ?";
		Tag tag = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
			    tag = new Tag();
				tag.setTag(rs.getString("tag"));
				System.out.println("Tag found:: " + tag);
			} else {
				System.out.println("Tag found with id= " + tag.getCategoryId());
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
		return tag;
	}

	@Override
	public void update(Tag tag) {
		String query = "update category set tag=? where category_id=?";
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, tag.getTag());
			ps.setInt(3, tag.getCategoryId());
			int out = ps.executeUpdate();
			if(out !=0){
				System.out.println("Tag updated with id=" + tag.getCategoryId());
			}else System.out.println("No tag found with id=" + tag.getCategoryId());
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteTagById(int id) {
		String query = "delete from category where id=?";
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			int out = ps.executeUpdate();
			if(out !=0){
				System.out.println("Tag deleted ");
			}else System.out.println("Tag found ");
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Tag> getAllTag() {
		String query = "SELECT * FROM category ORDER BY tag ASC";
		List<Tag> tagList = new ArrayList<Tag>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				Tag tag = new Tag();
				tag.setCategoryId(rs.getInt("category_id"));
				tag.setTag(rs.getString("tag"));
				tagList.add(tag);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tagList;
	}
}
