package com.picnews.jdbc.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

	private int id;
	private String login;
	private String pass;
	private String email;
	private String role;
	private String token, deviceId;
	private String photo;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPass(){
		return pass;			
	}
	
	public void setPass(String pass){
		this.pass = pass;				
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getToken(){
		return token;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public String getPhoto(){
		return photo;
	} 
	
	public void setPhoto(String photo){
		this.photo = photo;
	}
	
	public User() {
        
        id = -1;
        login = "";
        pass = "";
        role = "";
        token = "";
         
    }
 
	
	@Override
	public String toString(){
		return "{ID= " + id + " , Login = " + login + " , Password = " + pass + "Email = " + email + "Rola= " + role + "}";
	}
}
