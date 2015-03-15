package com.picnews.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.picnews.jdbc.model.Post;

public class PostDAOJDBCTemplateImpl implements PostDAO {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	@Transactional
	public void savePost(Post post) {
		String query = "insert into posts (user_id, content, photo, category_id, add_time, place, event_time) values (?,?,?,?,?,?,?)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		Object[] args = new Object[] {post.getUserId(), post.getContent(), post.getPhoto(), post.getCategory(), post.getAddTime(), post.getPlace(), post.getEventTime()};
		
		int out = jdbcTemplate.update(query, args);
		
		if(out !=0){
			System.out.println("Post saved with id= " + post.getPostId());
		}else System.out.println("Post save failed with id= " + post.getPostId());
	}

	@Override
	public Post getPostById(int id) {
		String query = "select post_id, user_id, content, photo, category_id, add_time, place, event_time from posts where post_id =( SELECT max(post_id)-? FROM posts)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
		//using RowMapper anonymous class, we can create a separate RowMapper for reuse
		Post post = jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Post>(){

			@Override
			public Post mapRow(ResultSet rs, int rowNum)
					throws SQLException {
						Post post = new Post();
						post.setPostId(rs.getInt("post_id"));
						post.setUserId(rs.getInt("user_id"));
						post.setContent(rs.getString("content"));
						post.setPhoto(rs.getBytes("photo"));
						post.setCategory(rs.getInt("category_id"));
						post.setAddTime(rs.getString("add_time"));
						post.setPlace(rs.getString("place"));
						post.setEventTime(rs.getString("event_time"));
						
				return post;
			}});		
		return post;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void update(Post post) {
		String query = "update posts set content=?, photo=? where post_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {post.getContent(), post.getPhoto(), post.getPostId()};
		
		int out = jdbcTemplate.update(query, args);
		if(out !=0){
			System.out.println("Post updated with id= " + post.getPostId());
		}else System.out.println("Post found with id= " + post.getPostId());
	}

	@Override
	public void deletePostById(int id) {

		String query = "delete from posts where post_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		int out = jdbcTemplate.update(query, id);
		if(out !=0){
			System.out.println("Post deleted with id= " + id);
		}else System.out.println("No post found with id= " + id);
	}

	@Override
	public List<Post> getAllPost() {
		String query = "select post_id, user_id, content, photo, category_id, add_time, place, event_time from posts";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Post> postList = new ArrayList<Post>();

		List<Map<String,Object>> postRows = jdbcTemplate.queryForList(query);
		
		for(Map<String,Object> postRow : postRows){
			Post post = new Post();
			post.setPostId(Integer.parseInt(String.valueOf(postRow.get("post_id"))));
			post.setUserId(Integer.parseInt(String.valueOf(postRow.get("user_id"))));
			post.setContent(String.valueOf(postRow.get("content")));
			post.setPhoto((byte[])postRow.get("photo"));
			post.setCategory(Integer.parseInt(String.valueOf(postRow.get("category_id"))));
			post.setAddTime(String.valueOf(postRow.get("add_time")));
			post.setPlace(String.valueOf(postRow.get("place")));			
			post.setEventTime(String.valueOf(postRow.get("event_time")));
			
			postList.add(post);
		}
		return postList;
	  
	}
}

