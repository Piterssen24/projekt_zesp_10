package com.picnews.dao;

import java.util.*;
import java.sql.*;

import javax.sql.DataSource;
import com.picnews.main.*;
import com.picnews.jdbc.model.User;

public class UserDAOImpl implements UserDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void saveUser(User user) {
		String query = "insert into users (id, login, pass, email, role) values (?,?,?,?,?)";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		Connection con = null;
		ResultSet rs = null;
		String query2 = "select MAX(id) from users";
		try{
			
			con = dataSource.getConnection();
			ps2 = con.prepareStatement(query2);
			rs = ps2.executeQuery();
			if(rs.next()){
				user.setId(rs.getInt("max(id)")+1);
			}
			ps = con.prepareStatement(query);
			ps.setInt(1, user.getId());
			ps.setString(2, user.getLogin());
			ps.setString(3, user.getPass());
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getRole());
			
			int out = ps.executeUpdate();
			
			if(out !=0){
				System.out.println("User saved with id= " + user.getId());
			}
			else System.out.println("User save failed with id= " + user.getId());
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		
		finally{
			try {
				ps2.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public User getByName(String name) {
		String query = "select id, login, pass, email, role, token from users where login = ? ";
		User user = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, name);
			rs = ps.executeQuery();
			user = new User();
			if(rs.next()){
				user.setId(rs.getInt("id"));
				user.setLogin(name);
				user.setPass(rs.getString("pass"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
				System.out.println("User Found:: " + user);
			}else{
				System.out.println("No user found with user_name= " + name);
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
		return user;
	}
	
	@Override
	public User getByEmail(String email) {
		String query = "select id, login, pass, email, role from users where email = ? ";
		User user = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, email);
			rs = ps.executeQuery();
			user = new User();
			if(rs.next()){
				user.setId(rs.getInt("id"));
				user.setLogin(rs.getString("login"));
				user.setEmail(email);
				user.setPass(rs.getString("pass"));
				user.setRole(rs.getString("role"));
				System.out.println("User Found:: " + user);
			}else{
				System.out.println("No user found with email= " + email);
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
		return user;
	}

	@Override
	public void update(User user, String token) {
		String query = "update users set token=? where login=?";
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, token);
			ps.setString(2, user.getLogin());
			int out = ps.executeUpdate();
			if(out !=0){
				System.out.println("Users updated with login=" + user.getLogin());
			}else System.out.println("No Employee found with login=" + user.getLogin());
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
	public void deleteById(int id) {
		String query = "delete from users where id=?";
		Connection con = null;
		PreparedStatement ps = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, id);
			int out = ps.executeUpdate();
			if(out !=0){
				System.out.println("Users deleted with id="+id);
			}else System.out.println("Users found with id="+id);
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
	public List<User> getAll() {
		String query = "select * from users";
		List<User> empList = new ArrayList<User>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				User usr = new User();
				usr.setId(rs.getInt("id"));
				usr.setPass(rs.getString("pass"));
				usr.setEmail(rs.getString("email"));
				usr.setLogin(rs.getString("login"));
				usr.setRole(rs.getString("role"));
				empList.add(usr);
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
		return empList;
	}
	
	@Override
	public User getByToken(String token){
		String query = "select * from users where token = ? ";
		User user = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, token);
			rs = ps.executeQuery();
			user = new User();
			if(rs.next()){
				user.setId(rs.getInt("id"));
				user.setLogin(rs.getString("login"));
				user.setPass(rs.getString("pass"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
				System.out.println("User Found:: " + user);
			}else{
				System.out.println("No user found with token= " + token);
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
		return user;
	}

}
