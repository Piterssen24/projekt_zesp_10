package com.picnews.dao;
import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.picnews.jdbc.model.Favourites;
import com.picnews.jdbc.model.Post;
import com.picnews.main.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FavouritesDAOJDBCImpl implements FavouritesDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	@Transactional
	public void saveFavourite(Favourites favourite) {
		String query = "insert into favourites (user_id, category_id) values (?,?)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		Object[] args = new Object[] {favourite.getUserId(), favourite.getCategoryId()};

		int out = jdbcTemplate.update(query, args);
		
		if(out !=0){
			System.out.println("Favourite saved with id= " + favourite.getFavId());
		}else System.out.println("Favourite save failed with id= " + favourite.getFavId());
	}

	@Override
	public List<Favourites> getByUserId(int id) {
		String query = "select * from favourites where user_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new Object[]{id}, new RowMapper<Favourites>(){
			@Override
			public Favourites mapRow(ResultSet rs, int rowNum)
					throws SQLException {
						Favourites fav = new Favourites();
						fav.setFavId(rs.getInt("fav_id"));		
						fav.setUserId(rs.getInt("user_id"));
						fav.setCategoryId(rs.getInt("category_id"));	
						
						return fav;
			}});				
	}		

	@Override
	public void deleteById(int id) {

		String query = "delete from favourites where user_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		int out = jdbcTemplate.update(query, id);
		if(out !=0){
			System.out.println("Favourite deleted with id= " + id);
		}else System.out.println("No favourite found with id= " + id);
	}

}
