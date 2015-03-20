package com.picnews.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

import javax.ws.rs.Path;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.ws.rs.*;

import com.picnews.dao.*;
import com.picnews.jdbc.model.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;
import com.picnews.jdbc.model.Post;
import com.picnews.jdbc.model.User;
import com.picnews.jdbc.model.Tag;
import org.codehaus.jettison.json.JSONArray;

 
@Path("/main")
public class SpringMain {
 
	public static String login;
	public static String password;
	public static String email;
	public static String role;
	public static Integer id, id2;
    public static Integer userId, tag;
    public static String content;
    public static String photo;
    public static String postType;
    public static String addTime;
    public static String place, location, eventTime, token;
    public byte[] databasePhoto = null;
	
    @Context
    UriInfo uriInfo;

    @Context
    Request request;
     
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Service is ready!";
    }
 
    /**
     * @return
     */
    @GET
    @Path("login")
    @Produces("application/json")
    public JSONArray getDatabaseUser(){
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	TagDAO tagDAO = (TagDAO) ctx.getBean("TagDAO", TagDAO.class);
    	FacultyDAO facultyDAO = (FacultyDAO) ctx.getBean("FacultyDAO", FacultyDAO.class);
    	JSONArray jarray = new JSONArray();
    	try{
    		User user = UsersDAO.getByName(login);
    		List<Tag> tag = tagDAO.getAllTag();
    		List<Faculty> faculty = facultyDAO.getAllFaculties();
    		Token t = new Token();
    		response = t.getToken();
    		User u = UsersDAO.getByToken(response);
    		while(u != null) {
        		response = t.getToken();
        		u = UsersDAO.getByToken(response);
    		}
    			if(login.equals(user.getLogin()) && password.equals(user.getPass())) {
    				System.out.println("Returning user: " + user.getLogin() + " " + user.getPass());
        			UsersDAO.update(user, response);
    				jarray.put(0, response);
    			
    				JSONArray jarrayTag = new JSONArray();
    				for(int i=0; i<tag.size(); i++){
    					JSONObject json = new JSONObject();
    					Tag tagi = tag.get(i);
    					json.put("categoryId", tagi.getCategoryId());
    					json.put("tag", tagi.getTag());
    					jarrayTag.put(json);
    				}
    			
    				JSONArray jarrayFac = new JSONArray();
    				for(int i=0; i<faculty.size(); i++){
    					JSONObject json = new JSONObject();
    					Faculty facultyi = faculty.get(i);
    					json.put("facultyId", facultyi.getFacultyId());
    					json.put("facultyName", facultyi.getFacultyName());
    					json.put("coordinates", facultyi.getCoordinates());
    					jarrayFac.put(json);
    				}

    				jarray.put(1, jarrayTag);
    				jarray.put(2, jarrayFac);
    			} else {
    				response = "";
    				jarray.put(0, response);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
   		}
   		ctx.close();
   		return jarray;
    }
    	
    @POST
    @Path("login2")
    @Consumes("application/json")
    public void getUser(JSONObject inputJsonObj) throws Exception {
    	  login = (String) inputJsonObj.get("login");
    	  password = (String) inputJsonObj.get("password");
    }
    
    @GET
    @Path("register")
    @Produces(MediaType.TEXT_PLAIN)
    public String insertDatabaseUser(){
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	User user = UsersDAO.getByName(login);
    	User user3 = UsersDAO.getByEmail(email);
    	if(user!=null){
    		response = "LOGINISTNIEJE";
    	}else if(user3!=null){
			response = "EMAILISTNIEJE";
    	}else{
    		User user2 = new User();
    		user2.setLogin(login);
    		user2.setPass(password);
    		user2.setEmail(email);
    		user2.setRole(role);
    		UsersDAO.saveUser(user2);
    		response = "TRUE";
    	}
        ctx.close();
        return response;
    }   
    
    @POST
    @Path("register2")
    @Consumes("application/json")
    public void getRegisterUser(JSONObject inputJsonObj) throws Exception {
    	  login = (String) inputJsonObj.get("login");
    	  password = (String) inputJsonObj.get("password");
    	  email = (String) inputJsonObj.get("email");
    	  role = (String) inputJsonObj.get("role");
    }

    @GET
    @Path("news")
    @Produces("application/json")
    public JSONArray returnNews(){
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	JSONArray jarray = new JSONArray();
    	int count=10;
    	for(int i=0;i<count;i++){ 
    		id2=id;
    		id=id+i;
    		Post post = postDAO.getPostById(id);
    		if(post == null){
    			count = count+1;
    		} else {
    			JSONObject json = new JSONObject();
    			try{
    				databasePhoto = null;
    				databasePhoto = post.getPhoto();
    				photo = encodeImage(databasePhoto);
    				json.put("postId",post.getPostId());
    				json.put("userLogin",post.getUserLogin());
    				json.put("content",post.getContent());
    				json.put("photo", photo);
    				json.put("categoryId", "6");
    				json.put("addTime",post.getAddTime());
    				json.put("place",post.getPlace());
    				json.put("eventTime", post.getEventTime());
    				json.put("count", count);
    				if(post.getPostId() == 1){
    					i = count;
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			jarray.put(json);
    		}
    		id = id2;
    	}
    	ctx.close();
    	return jarray;	
    }

    @POST
    @Path("news2")
    @Consumes("application/json")
    public void getNews(JSONObject inputJsonObj) throws Exception {
    	id = (Integer) inputJsonObj.get("id");
    }
    
    @GET
    @Path("post")
    @Produces(MediaType.TEXT_PLAIN)
    public String addPost(){
    	databasePhoto = decodeImage(photo);
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	try{
    		PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    		UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    		User user = UsersDAO.getByToken(token);
    		Post post = new Post();
    		post.setUserId(user.getId());
    		post.setContent(content);
    		post.setPhoto(databasePhoto);
    		post.setCategory(tag);
    		post.setAddTime(addTime);
    		post.setPlace(place);
    		post.setEventTime(eventTime);
    		System.out.println("Registering");
    		postDAO.savePost(post);
    		System.out.println("Registering post...");
    		response = "TRUE";
    	} catch(Exception e){
    		e.printStackTrace();
    	}
        ctx.close();
        return response;
    }

    @POST
    @Path("post2")
    @Consumes("application/json")
    public void getPostInfo(JSONObject inputJsonObj) throws Exception {
  	    token = (String) inputJsonObj.get("token");
  	  	content = (String) inputJsonObj.get("content");
  	  	photo = (String) inputJsonObj.get("photo");
  	  	addTime = (String) inputJsonObj.get("addTime");
  	  	place = (String) inputJsonObj.get("place");
  	  	eventTime = (String) inputJsonObj.get("eventTime");
  	  	tag = Integer.parseInt((String) inputJsonObj.get("tag"));
    }

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }
     
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }

}