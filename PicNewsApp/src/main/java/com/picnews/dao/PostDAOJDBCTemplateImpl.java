package com.picnews.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

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
	
	public int getMaxId(){
		String query = "Select Max(post_id) from posts";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForInt(query);
	}

	@Override
	public List<Post> getPostById(int id) {
		String query = "select * from posts INNER JOIN users ON users.id = posts.user_id where post_id <= ? order by post_id DESC limit 10";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new Object[]{id}, new RowMapper<Post>(){
			 @Override  
			    public Post mapRow(ResultSet rs, int rownumber) throws SQLException {  
				 	Post post = new Post(); 
				 	post.setPostId(rs.getInt("post_id"));
					post.setUserLogin(rs.getString("login"));
					post.setUserId(rs.getInt("user_id"));
					post.setContent(rs.getString("content"));
					post.setPhoto(rs.getString("photo"));
					post.setCategory(rs.getInt("category_id"));
					post.setAddTime(rs.getString("add_time"));
					post.setPlace(rs.getString("place"));			
					post.setEventTime(rs.getString("event_time"));
			        return post;  
			    }  
		});
	}
	
	@Override
	public List<Post> getPostByCategoryId(int id, int tag) {
		String query = "select * from posts INNER JOIN users ON users.id = posts.user_id where post_id <= ? and category_id = ? order by post_id DESC limit 10";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new Object[]{id, tag}, new RowMapper<Post>(){
			@Override
			public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
						Post post = new Post();
						post.setPostId(rs.getInt("post_id"));
						post.setUserLogin(rs.getString("login"));
						post.setUserId(rs.getInt("user_id"));
						post.setContent(rs.getString("content"));
						post.setPhoto(rs.getString("photo"));
						post.setCategory(rs.getInt("category_id"));
						post.setAddTime(rs.getString("add_time"));
						post.setPlace(rs.getString("place"));
						post.setEventTime(rs.getString("event_time"));
						return post;
			}
		});		
	}
	
	@Override
	public List<Post> getNearlyPosts(int id) {
		String query = "select * from posts INNER JOIN users ON users.id = posts.user_id where post_id <= ? and event_time between curdate() and DATE_ADD(curdate(), INTERVAL 3 DAY) order by post_id DESC limit 10";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new Object[]{id}, new RowMapper<Post>(){
			@Override
			public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
						Post post = new Post();
						post.setPostId(rs.getInt("post_id"));
						post.setUserLogin(rs.getString("login"));
						post.setUserId(rs.getInt("user_id"));
						post.setContent(rs.getString("content"));
						post.setPhoto(rs.getString("photo"));
						post.setCategory(rs.getInt("category_id"));
						post.setAddTime(rs.getString("add_time"));
						post.setPlace(rs.getString("place"));
						post.setEventTime(rs.getString("event_time"));
						return post;
			}
		});		
	}
		
		@Override
		public List<Post> getPostByFavIds(Object[] favCategoryId) {
			String query = "SELECT * FROM posts INNER JOIN users ON users.id = posts.user_id where category_id in (";
			String temp="";
			for(int i=0; i<favCategoryId.length-1; i++)
			{
			  temp+=",?";
			}
			temp=temp.replaceFirst(",","");
			temp+=") and post_id <= ? order by post_id DESC limit 10";
			query=query+temp;
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			return jdbcTemplate.query(query, favCategoryId, new RowMapper<Post>(){
				@Override
				public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
							Post post = new Post();
							post.setPostId(rs.getInt("post_id"));
							post.setUserLogin(rs.getString("login"));
							post.setUserId(rs.getInt("user_id"));
							post.setContent(rs.getString("content"));
							post.setPhoto(rs.getString("photo"));
							post.setCategory(rs.getInt("category_id"));
							post.setAddTime(rs.getString("add_time"));
							post.setPlace(rs.getString("place"));
							post.setEventTime(rs.getString("event_time"));
							
					return post;
				}});		
		}
		
		@Override
		public List<Post> getPostByFolIds(Object[] folUserId) {
			String query = "SELECT * FROM posts INNER JOIN users ON users.id = posts.user_id where user_id in (";
			String temp="";
			for(int i=0; i<folUserId.length-1; i++)
			{
			  temp+=",?";
			}
			temp=temp.replaceFirst(",","");
			temp+=") and post_id <= ? order by post_id DESC limit 10";
			query=query+temp;
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			return jdbcTemplate.query(query, folUserId, new RowMapper<Post>(){
				@Override
				public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
							Post post = new Post();
							post.setPostId(rs.getInt("post_id"));
							post.setUserLogin(rs.getString("login"));
							post.setUserId(rs.getInt("user_id"));
							post.setContent(rs.getString("content"));
							post.setPhoto(rs.getString("photo"));
							post.setCategory(rs.getInt("category_id"));
							post.setAddTime(rs.getString("add_time"));
							post.setPlace(rs.getString("place"));
							post.setEventTime(rs.getString("event_time"));
							
					return post;
				}});		
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
	public void reportPostById(int postId) {
		String query = "update posts set reported=reported+1 where post_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {postId};
		
		int out = jdbcTemplate.update(query, args);
		if(out !=0){
			System.out.println("Post updated with id= " + postId);
		}else System.out.println("Post found with id= " + postId);
	}
	
	@Override
	public void updatePhoto(int postId, String data) {
		String query = "update posts set photo=? where post_id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {data, postId};
		
		int out = jdbcTemplate.update(query, args);
		if(out !=0){
			System.out.println("Post updated with id= " + postId);
		}else System.out.println("Post found with id= " + postId);
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
		String query = "select * from posts INNER JOIN users ON users.id = posts.user_id where event_time >= sysdate()";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new RowMapper<Post>(){
			 @Override  
			    public Post mapRow(ResultSet rs, int rownumber) throws SQLException {  
				 	Post post = new Post(); 
				 	post.setPostId(rs.getInt(1));
					post.setUserLogin(rs.getString("login"));
					post.setContent(rs.getString(3));
					post.setPhoto(rs.getString(4));
					post.setCategory(rs.getInt(5));
					post.setAddTime(rs.getString(6));
					post.setPlace(rs.getString(7));			
					post.setEventTime(rs.getString(8));
			        return post;  
			    }  
		});
		
	}

	
	@Override
	public List<Post> getPostByUserId(int id) {
		String query = "select * from posts where user_id = ? order by post_id desc";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(query, new Object[]{id}, new RowMapper<Post>(){
			@Override
			public Post mapRow(ResultSet rs, int rowNum)
					throws SQLException {
						Post post = new Post();
						post.setPostId(rs.getInt("post_id"));
						post.setContent(rs.getString("content"));
						post.setPhoto(rs.getString("photo"));
						post.setCategory(rs.getInt("category_id"));
						post.setAddTime(rs.getString("add_time"));
						post.setPlace(rs.getString("place"));			
						post.setEventTime(rs.getString("event_time"));												
						return post;
			}});		
		
	}
}

