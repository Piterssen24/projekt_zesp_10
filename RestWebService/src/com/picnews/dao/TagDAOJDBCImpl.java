package com.picnews.dao;
import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.picnews.jdbc.model.Tag;

public class TagDAOJDBCImpl implements TagDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	
	@Override
	public void saveTag(Tag tag) {
		String query = "insert into category (tag) values (?)";	
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
		Object[] args = new Object[] {tag.getTag()};	
		int out = jdbcTemplate.update(query, args);
		if(out !=0) {
			System.out.println("Tag saved with id= " + tag.getCategoryId());
		} else System.out.println("Tag save failed with id= " + tag.getCategoryId());
	}

	@Override
	public Tag getTagById(int id) {
		String query = "select category_id, tag from category where category_id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);		
		//using RowMapper anonymous class, we can create a separate RowMapper for reuse
		Tag tag = jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Tag>(){
			@Override
			public Tag mapRow(ResultSet rs, int rowNum)
					throws SQLException {
						Tag tag = new Tag();
						tag.setCategoryId(rs.getInt("category_id"));
						tag.setTag(rs.getString("tag"));
				return tag;
			}});		
		return tag;
	}
	

	@Override
	public void update(Tag tag) { 
		String query = "update category set tag=? where category_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {tag.getCategoryId()};	
		int out = jdbcTemplate.update(query, args);
		if(out !=0){
			System.out.println("Tag updated with id= " + tag.getCategoryId());
		}else System.out.println("Tag found with id= " + tag.getCategoryId()); 
	}

	@Override
	public void deleteTagById(int id) {
		String query = "delete from category where category_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
		int out = jdbcTemplate.update(query, id);
		if(out !=0){
			System.out.println("Tag deleted with id= " + id);
		}else System.out.println("No tag found with id= " + id);
	}

	@Override
	public List<Tag> getAllTag() {
		String query = "select * from category";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new RowMapper<Tag>(){  
			@Override  
			public Tag mapRow(ResultSet rs, int rownumber) throws SQLException {  
				Tag tag = new Tag();  
				tag.setCategoryId(rs.getInt(1));  
				tag.setTag(rs.getString(2));  
				return tag;  
		    }  
		});   			  
	}
}

