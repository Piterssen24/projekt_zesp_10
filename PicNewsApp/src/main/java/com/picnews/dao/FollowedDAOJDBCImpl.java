package com.picnews.dao;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.picnews.jdbc.model.Followed;
import com.picnews.main.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FollowedDAOJDBCImpl implements FollowedDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	@Transactional
	public void saveFollow(int userId, int followedUserId) {
		String query = "insert into follows (user_id, followed_user_id) values (?,?)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		Object[] args = new Object[] {userId, followedUserId};

		int out = jdbcTemplate.update(query, args);
		
	/*	if(out !=0){
			System.out.println("Follow saved with id= " + favourite.getFavId());
		}else System.out.println("Follow save failed with id= " + favourite.getFavId());*/
	}

	@Override
	public List<Followed> getByUserId(int id) {
		String query = "select * from follows where user_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new Object[]{id}, new RowMapper<Followed>(){
			@Override
			public Followed mapRow(ResultSet rs, int rowNum)
					throws SQLException {
						Followed fol = new Followed();
						fol.setUserId(rs.getInt("user_id"));
						fol.setFollowedUserId(rs.getInt("followed_user_id"));	
						
						return fol;
			}});				
	}		

	@Override
	public void deleteByUserId(int userId, int followedUserId) {

		String query = "delete from follows where user_id=? and followed_user_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		int out = jdbcTemplate.update(query, userId, followedUserId);
	/*	if(out !=0){
			System.out.println("Favourite deleted with id= " + id);
		}else System.out.println("No favourite found with id= " + id);*/
	}

}
