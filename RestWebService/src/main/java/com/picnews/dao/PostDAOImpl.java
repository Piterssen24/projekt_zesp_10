package com.picnews.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import com.picnews.jdbc.model.Post;

public class PostDAOImpl implements PostDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void savePost(Post post) {
		String query = "insert into posts (user_id, content, photo, category_id, add_time, place,event_time) values (?,?,?,?,?,?,?)";
		PreparedStatement ps = null;
		Connection con = null;
		
		try{
			
			con = dataSource.getConnection();
			
			ps = con.prepareStatement(query);
			ps.setInt(1, post.getUserId());
			ps.setString(2, post.getContent());
			ps.setBytes(3, post.getPhoto());
			ps.setInt(4, post.getCategory());
			ps.setString(5, post.getAddTime());
			ps.setString(6, post.getPlace());
			ps.setString(7, post.getEventTime());
			
			int out = ps.executeUpdate();
			
			if(out !=0){
				System.out.println("Post saved with id= " + post.getPostId());
			}
			else System.out.println("Post save failed with id= " + post.getPostId());
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		
		finally{
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Post getPostById(int id) {
		String query = "SELECT * FROM posts INNER JOIN users ON users.id = posts.user_id where post_id=( SELECT max(post_id)-? FROM posts)";
		Post post = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
			    post = new Post();
				post.setPostId(rs.getInt("post_id"));
				post.setUserLogin(rs.getString("login"));
				post.setContent(rs.getString("content"));
				post.setPhoto(rs.getBytes("photo"));
				post.setCategory(rs.getInt("category"));
				post.setAddTime(rs.getString("add_time"));
				post.setPlace(rs.getString("place"));
				post.setEventTime(rs.getString("event_time"));
				System.out.println("Post found:: " + post);
			}else{
				System.out.println("No post found with id= " + id);
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
		return post;
	}

	@Override
	public void update(Post post) {
		String query = "update posts set content=?, photo=? where post_id=?";
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, post.getContent());
			ps.setBytes(2, post.getPhoto());
			ps.setInt(3, post.getPostId());
			int out = ps.executeUpdate();
			if(out !=0){
				System.out.println("Post updated with id=" + post.getPostId());
			}else System.out.println("No post found with id=" + post.getPostId());
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
	public void deletePostById(int id) {
		String query = "delete from posts where post_id=?";
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			int out = ps.executeUpdate();
			if(out !=0){
				System.out.println("Post deleted with id="+id);
			}else System.out.println("Post found with id="+id);
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
	public List<Post> getAllPost() {
		String query = "select * from posts";
		List<Post> postList = new ArrayList<Post>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				Post post = new Post();
				post.setPostId(rs.getInt("post_id"));
				post.setContent(rs.getString("content"));
				post.setPhoto(rs.getBytes("photo"));
				post.setCategory(rs.getInt("category"));
				post.setAddTime(rs.getString("add_time"));
				post.setPlace(rs.getString("place"));
				post.setEventTime(rs.getString("place"));
				postList.add(post);
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
		return postList;
	}
}
