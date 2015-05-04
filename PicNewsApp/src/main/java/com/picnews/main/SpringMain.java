package com.picnews.main;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.picnews.dao.FacultyDAO;
import com.picnews.dao.PostDAO;
import com.picnews.dao.TagDAO;
import com.picnews.dao.UserDAO;
import com.picnews.dao.FollowedDAO;
import com.picnews.dao.FavouritesDAO;
import com.picnews.dao.ReportedDAO;
import com.picnews.jdbc.model.Faculty;
import com.picnews.jdbc.model.Post;
import com.picnews.jdbc.model.Tag;
import com.picnews.jdbc.model.Followed;
import com.picnews.jdbc.model.User;
import com.picnews.jdbc.model.Favourites;
import com.picnews.jdbc.model.Reported;
import com.picnews.main.Token;

 
@Path("/main")
public class SpringMain {
 
	public static String login, deviceId;
	public static String password, cryptedpass;
	public static String email;
	public static String role;
	public static Integer id, id2, postId;
    public static Integer userId, tag;
    public static String content;
    public static String photo, photou;
    public static String postType;
    public static String addTime, uPhoto, userLogin;
    public static String place, location, eventTime, token, authorLogin;
    public byte[] databasePhoto = null;
    public byte[] userPhoto = null;
    public static List<Integer> favouritesTags;
    public String[] favCategoryId;
    public static Object[] obj;
    public static JSONArray ja;
    private final static String ALGORITHM = "AES";
	private final static String HEX = "0123456789ABCDEF";
	private String key = "key-0123123451";
	public String photoUrl = "http://158.75.2.10:10000/photos/photo";
	
    @Context
    UriInfo uriInfo;

    @Context
    Request request;
     
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
    /*	SavePhoto sf = new SavePhoto("a","b");
    	try {
			sf.saveToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return "Service is ready!";
    }
 
    /**
     * metoda wysylajaca do aplikacji odpowiedz czy uzytkownik o danym loginie i hasle istnieje czy nie, 
     * pobiera z bazy liste tagow i wydzialow, aktualizuje token dla danego uzytkownika
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
    		if(login.equals(user.getLogin()) && password.equals(user.getPass())) {
    		if(user.getDeviceId().equals("")){
    			UsersDAO.saveDevice(deviceId, user.getId());
    		}
    		user = UsersDAO.getByName(login);
    		if(user.getDeviceId().equals(deviceId)){
    		List<Tag> tag = tagDAO.getAllTag();
    		List<Faculty> faculty = facultyDAO.getAllFaculties();
    		Token t = new Token();
    		response = t.getToken();
    		User u = UsersDAO.getByToken(response);
    		while(u != null) {
        		response = t.getToken();
        		u = UsersDAO.getByToken(response);
    		}
    			//if(login.equals(user.getLogin()) && password.equals(user.getPass())) {
        			UsersDAO.update(user, response);
    				jarray.put(0, response);
    				jarray.put(1, user.getRole());
    			
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

    				jarray.put(2, jarrayTag);
    				jarray.put(3, jarrayFac);
    		}
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
    	
    /**
     * odbiera od aplikacji login i haslo 
     * */
    @POST
    @Path("login2")
    @Consumes("application/json")
    public void getUser(JSONObject inputJsonObj) throws Exception {
    	  login = (String) inputJsonObj.get("login");
    	  deviceId = (String) inputJsonObj.get("deviceId");
    	  password = (String) inputJsonObj.get("password");
    	 /* try{
    		  password = decipher(key, cryptedpass);
    	  } catch (Exception e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		  }*/
    }
    
    @GET
    @Path("logout")
    @Produces(MediaType.TEXT_PLAIN)
    public String clearDevice(){
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	try{
    		User user = UsersDAO.getByName(login);
    		UsersDAO.saveDevice("", user.getId());
    		response = "TRUE";
    	} catch(Exception e){
    		e.printStackTrace();
   		}
    	ctx.close();
   		return response;
    }
    	
    /**
     * odbiera od aplikacji login i haslo 
     * */
    @POST
    @Path("logout2")
    @Consumes("application/json")
    public void getLogin(JSONObject inputJsonObj) throws Exception {
    	  login = (String) inputJsonObj.get("userLogin");
    }
    
    /**
     * rejestruje uzytkownika w bazie, dodaje
     * @throws URISyntaxException 
     * @throws IOException 
     * */
    @GET
    @Path("register")
    @Produces(MediaType.TEXT_PLAIN)
    public String insertDatabaseUser() throws IOException, URISyntaxException{
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	User user = UsersDAO.getByName(login);
    	System.out.println("1");
    	User user3 = UsersDAO.getByEmail(email);
    	System.out.println("2");
    	databasePhoto = decodeImage(uPhoto);
    	System.out.println("3");
    	if(user!=null){
    		response = "LOGINISTNIEJE";
    	}else if(user3!=null){
			response = "EMAILISTNIEJE";
    	}else{
    		User user2 = new User();
    		System.out.println("4");
    		user2.setLogin(login);
    		user2.setPass(password);
    		user2.setEmail(email);
    		user2.setRole(role);
    		//user2.setPhoto(databasePhoto);
    		user2.setPhoto("user"+login);
    		System.out.println("5");
    		UsersDAO.saveUser(user2);
    		SavePhoto sf = new SavePhoto(databasePhoto, "user"+login);
    		sf.saveUserToFile();
    		System.out.println("6");
    		response = "TRUE";
    	}
    	databasePhoto = null;
        ctx.close();
        return response;
    }   
    
    /**
     * metoda odbiera dane do rejestracji, prypisuje zmiennym wartosci odebrane z aplikacji
     * */
    @POST
    @Path("register2")
    @Consumes("application/json")
    public void getRegisterUser(JSONObject inputJsonObj) throws Exception {
    	System.out.println("0");
    	  login = (String) inputJsonObj.get("login");
    	  password = (String) inputJsonObj.get("password");
    	 /* try{
    		  password = decipher(key, cryptedpass);
    	  } catch (Exception e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		  }*/
    	  email = (String) inputJsonObj.get("email");
    	  role = (String) inputJsonObj.get("role");
    	  uPhoto = (String) inputJsonObj.getString("userPhoto");
    }

    /**
     * pobiera z bazy posty i liste ulubionych tagow wysyla do aplikacji
     * */ 
    @GET
    @Path("news")
    @Produces("application/json")
    public JSONArray returnNews(){
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	ReportedDAO reportDAO = (ReportedDAO) ctx.getBean("ReportedDAO", ReportedDAO.class);
    	FollowedDAO followDAO = (FollowedDAO) ctx.getBean("FollowedDAO", FollowedDAO.class);
    	FavouritesDAO favDAO = (FavouritesDAO) ctx.getBean("FavouritesDAO", FavouritesDAO.class);
    	JSONArray jarray = new JSONArray();
    	JSONArray jarrayPosts = new JSONArray();
    	JSONArray jarrayFav = new JSONArray();
    	JSONArray jarrayRep = new JSONArray();
    	JSONArray jarrayFol = new JSONArray();
    	User u = UsersDAO.getByToken(token);
    	List<Followed> fol = followDAO.getByUserId(u.getId());
    	String myLogin = u.getLogin();
    	for(int j=0; j<fol.size(); j++){
			JSONObject json = new JSONObject();
			Followed foli = fol.get(j);
			User us = UsersDAO.getById(foli.getFollowedUserId());
			try {
    			json.put("folUserName",us.getLogin());
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFol.put(json);
			
		}
    	List<Reported> rep = reportDAO.getReportByUserId(u.getId());
    	for(int j=0; j<rep.size(); j++){
			JSONObject json = new JSONObject();
			Reported repi = rep.get(j);
			try {
				json.put("repUserId",Integer.toString(repi.getUserId()));
    			json.put("repPostId",Integer.toString(repi.getPostId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayRep.put(json);
			
		}
    	List<Favourites> fav = favDAO.getByUserId(u.getId());
		for(int j=0; j<fav.size(); j++){
			JSONObject json = new JSONObject();
			Favourites favi = fav.get(j);
			try {
				json.put("favUserId",Integer.toString(favi.getUserId()));
    			json.put("favCategoryId",Integer.toString(favi.getCategoryId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFav.put(json);
			
		}
    		List<Post> posts = postDAO.getPostById(id);
    		for(int i=0; i<posts.size(); i++){
    			Post post = posts.get(i);
    			JSONObject json = new JSONObject();
    			try{
    				//databasePhoto = null;
    				//databasePhoto = post.getPhoto();
    				//SavePhoto sf = new SavePhoto("photo"+post.getPostId());
    				//databasePhoto = sf.readFromFile();
    				//photo = encodeImage(databasePhoto);
    				json.put("postId",post.getPostId());
    				json.put("userLogin",post.getUserLogin());
    				json.put("content",post.getContent());
    				json.put("photo", post.getPhoto());
    				json.put("categoryId", post.getCategory());
    				json.put("addTime",post.getAddTime());
    				json.put("place",post.getPlace());
    				json.put("eventTime", post.getEventTime());
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			jarrayPosts.put(json);
    		}
    	jarray.put(jarrayFav);
    	jarray.put(jarrayPosts);
    	jarray.put(jarrayRep);
    	jarray.put(jarrayFol);
    	jarray.put(myLogin);
    	ctx.close();
    	return jarray;	
    }

    /**
     * pobiera id ostatniego posta ktory zostal ostatnio wyslany do aplikacji oraz token 
     * */
    @POST
    @Path("news2")
    @Consumes("application/json")
    public void getNews(JSONObject inputJsonObj) throws Exception {
    	String stringId = (String) inputJsonObj.get("id");
    	if(stringId.equals("")){
    		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    		PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    		id = postDAO.getMaxId();
    		ctx.close();
    	} else {
    		id = Integer.parseInt(stringId);
    	}
    	token = (String) inputJsonObj.get("token");
    }
    
    /**
     * pobiera z bazy newsy nalezace do ulubionych kategorii danego uzytkownika i wysyla do aplikacji
     * */
    @GET
    @Path("newsFavourites")
    @Produces("application/json")
    public JSONArray returnFavouritesNews(){
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	ReportedDAO reportDAO = (ReportedDAO) ctx.getBean("ReportedDAO", ReportedDAO.class);
    	FollowedDAO followDAO = (FollowedDAO) ctx.getBean("FollowedDAO", FollowedDAO.class);
    	FavouritesDAO favDAO = (FavouritesDAO) ctx.getBean("FavouritesDAO", FavouritesDAO.class);
    	JSONArray jarray = new JSONArray();
    	JSONArray jarrayPosts = new JSONArray();
    	JSONArray jarrayFav = new JSONArray();
    	JSONArray jarrayRep = new JSONArray();
    	JSONArray jarrayFol = new JSONArray();
    	User u = UsersDAO.getByToken(token);
    	String myLogin = u.getLogin();
    	List<Followed> fol = followDAO.getByUserId(u.getId());
    	for(int j=0; j<fol.size(); j++){
			JSONObject json = new JSONObject();
			Followed foli = fol.get(j);
			User us = UsersDAO.getById(foli.getFollowedUserId());
			try {
    			json.put("folUserName",us.getLogin());
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFol.put(json);
			
		}
    	List<Reported> rep = reportDAO.getReportByUserId(u.getId());
    	for(int j=0; j<rep.size(); j++){
			JSONObject json = new JSONObject();
			Reported repi = rep.get(j);
			try {
				json.put("repUserId",Integer.toString(repi.getUserId()));
    			json.put("repPostId",Integer.toString(repi.getPostId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayRep.put(json);
			
		}
    	List<Favourites> fav = favDAO.getByUserId(u.getId());
		for(int j=0; j<fav.size(); j++){
			JSONObject json = new JSONObject();
			Favourites favi = fav.get(j);
			try {
				json.put("favUserId",Integer.toString(favi.getUserId()));
    			json.put("favCategoryId",Integer.toString(favi.getCategoryId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFav.put(json);
			
		}
    		obj[ja.length()] = id;
    		List<Post> posts = postDAO.getPostByFavIds(obj);
    		for(int i=0; i<posts.size(); i++){
    			Post post = posts.get(i);
    			JSONObject json = new JSONObject();
    			try{
    				//databasePhoto = null;
    				//databasePhoto = post.getPhoto();
    				//SavePhoto sf = new SavePhoto("photo"+post.getPostId());
    				//databasePhoto = sf.readFromFile();
    				//photo = encodeImage(databasePhoto);
    				json.put("postId",post.getPostId());
    				json.put("userLogin",post.getUserLogin());
    				json.put("content",post.getContent());
    				json.put("photo", post.getPhoto());
    				json.put("categoryId", post.getCategory());
    				json.put("addTime",post.getAddTime());
    				json.put("place",post.getPlace());
    				json.put("eventTime", post.getEventTime());
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			jarrayPosts.put(json);
    		}
    	jarray.put(jarrayFav);
    	jarray.put(jarrayPosts);
    	jarray.put(jarrayRep);
    	jarray.put(jarrayFol);
    	jarray.put(myLogin);
    	ctx.close();
    	return jarray;	
    }

    /**
     * pobiera id najstarszego wczytanego posta w aplikacji, token uzytkownika, liste ulubionych tagow
     * */
    @POST
    @Path("newsFavourites2")
    @Consumes("application/json")
    public void getFavouritesNews(JSONArray inputJsonObj) throws Exception {
    	String stringId = (String) inputJsonObj.get(0);
    	if(stringId.equals("")){
    		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    		PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    		id = postDAO.getMaxId();
    		ctx.close();
    	} else {
    		id = Integer.parseInt(stringId);
    	}
    	token = (String) inputJsonObj.get(1);
    	ja = inputJsonObj.getJSONArray(2);
    	obj = new Object[ja.length()+1];
    	for(int i=0; i<ja.length(); i++){
    		obj[i] = ja.get(i);
    	}
    }
    
    /**
     * metoda odpowiedzialna za pobieranie z bazy postow wedlug wybranej przez uzytkownika kategorii
     * */
    @GET
    @Path("newsFiltered")
    @Produces("application/json")
    public JSONArray returnFilteredNews(){
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	ReportedDAO reportDAO = (ReportedDAO) ctx.getBean("ReportedDAO", ReportedDAO.class);
    	FollowedDAO followDAO = (FollowedDAO) ctx.getBean("FollowedDAO", FollowedDAO.class);
    	FavouritesDAO favDAO = (FavouritesDAO) ctx.getBean("FavouritesDAO", FavouritesDAO.class);
    	JSONArray jarray = new JSONArray();
    	JSONArray jarrayPosts = new JSONArray();
    	JSONArray jarrayFav = new JSONArray();
    	JSONArray jarrayRep = new JSONArray();
    	JSONArray jarrayFol = new JSONArray();
    	User u = UsersDAO.getByToken(token);
    	String myLogin = u.getLogin();
    	List<Followed> fol = followDAO.getByUserId(u.getId());
    	for(int j=0; j<fol.size(); j++){
			JSONObject json = new JSONObject();
			Followed foli = fol.get(j);
			User us = UsersDAO.getById(foli.getFollowedUserId());
			try {
    			json.put("folUserName",us.getLogin());
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFol.put(json);
			
		}
    	List<Reported> rep = reportDAO.getReportByUserId(u.getId());
    	for(int j=0; j<rep.size(); j++){
			JSONObject json = new JSONObject();
			Reported repi = rep.get(j);
			try {
				json.put("repUserId",Integer.toString(repi.getUserId()));
    			json.put("repPostId",Integer.toString(repi.getPostId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayRep.put(json);
			
		}
    	List<Favourites> fav = favDAO.getByUserId(u.getId());
		for(int j=0; j<fav.size(); j++){
			JSONObject json = new JSONObject();
			Favourites favi = fav.get(j);
			try {
				json.put("favUserId",Integer.toString(favi.getUserId()));
    			json.put("favCategoryId",Integer.toString(favi.getCategoryId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFav.put(json);
			
		}
    		List<Post> posts = postDAO.getPostByCategoryId(id, tag);
    		for(int i=0; i<posts.size(); i++){
	    		Post post = posts.get(i);
    			JSONObject json = new JSONObject();
    			try{
    				//databasePhoto = null;
    				//databasePhoto = post.getPhoto();
    				//SavePhoto sf = new SavePhoto("photo"+post.getPostId());
    				//databasePhoto = sf.readFromFile();
    				//photo = encodeImage(databasePhoto);
    				json.put("postId",post.getPostId());
    				json.put("userLogin",post.getUserLogin());
    				json.put("content",post.getContent());
    				json.put("photo", post.getPhoto());
    				json.put("categoryId", post.getCategory());
    				json.put("addTime",post.getAddTime());
    				json.put("place",post.getPlace());
    				json.put("eventTime", post.getEventTime());
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			jarrayPosts.put(json);
    		}
    	jarray.put(jarrayFav);
    	jarray.put(jarrayPosts);
    	jarray.put(jarrayRep);
    	jarray.put(jarrayFol);
    	jarray.put(myLogin);
    	ctx.close();
    	return jarray;	
    }

    /**
     * pobiera id najstarszego posta w aplikacji,token uzytkownika i id kategorii wedlug ktorej maja byc wyswietlane posty
     * */
    @POST
    @Path("newsFiltered2")
    @Consumes("application/json")
    public void getFilteredNews(JSONObject inputJsonObj) throws Exception {
    	String stringId = (String) inputJsonObj.get("id");
    	if(stringId.equals("")){
    		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    		PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    		id = postDAO.getMaxId();
    		ctx.close();
    	} else {
    		id = Integer.parseInt(stringId);
    	}
    	token = (String) inputJsonObj.get("token");
    	tag = Integer.parseInt((String) inputJsonObj.get("tag"));
    }
    
    @GET
    @Path("news3Days")
    @Produces("application/json")
    public JSONArray return3DaysNews(){
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	ReportedDAO reportDAO = (ReportedDAO) ctx.getBean("ReportedDAO", ReportedDAO.class);
    	FollowedDAO followDAO = (FollowedDAO) ctx.getBean("FollowedDAO", FollowedDAO.class);
    	FavouritesDAO favDAO = (FavouritesDAO) ctx.getBean("FavouritesDAO", FavouritesDAO.class);
    	JSONArray jarray = new JSONArray();
    	JSONArray jarrayPosts = new JSONArray();
    	JSONArray jarrayFav = new JSONArray();
    	JSONArray jarrayRep = new JSONArray();
    	JSONArray jarrayFol = new JSONArray();
    	User u = UsersDAO.getByToken(token);
    	String myLogin = u.getLogin();
    	List<Followed> fol = followDAO.getByUserId(u.getId());
    	for(int j=0; j<fol.size(); j++){
			JSONObject json = new JSONObject();
			Followed foli = fol.get(j);
			User us = UsersDAO.getById(foli.getFollowedUserId());
			try {
    			json.put("folUserName",us.getLogin());
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFol.put(json);
			
		}
    	List<Reported> rep = reportDAO.getReportByUserId(u.getId());
    	for(int j=0; j<rep.size(); j++){
			JSONObject json = new JSONObject();
			Reported repi = rep.get(j);
			try {
				json.put("repUserId",Integer.toString(repi.getUserId()));
    			json.put("repPostId",Integer.toString(repi.getPostId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayRep.put(json);
			
		}
    	List<Favourites> fav = favDAO.getByUserId(u.getId());
		for(int j=0; j<fav.size(); j++){
			JSONObject json = new JSONObject();
			Favourites favi = fav.get(j);
			try {
				json.put("favUserId",Integer.toString(favi.getUserId()));
    			json.put("favCategoryId",Integer.toString(favi.getCategoryId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFav.put(json);
			
		}
    		List<Post> posts = postDAO.getNearlyPosts(id);
    		for(int i=0; i<posts.size(); i++){
	    		Post post = posts.get(i);
    			JSONObject json = new JSONObject();
    			try{
    				json.put("postId",post.getPostId());
    				json.put("userLogin",post.getUserLogin());
    				json.put("content",post.getContent());
    				json.put("photo", post.getPhoto());
    				json.put("categoryId", post.getCategory());
    				json.put("addTime",post.getAddTime());
    				json.put("place",post.getPlace());
    				json.put("eventTime", post.getEventTime());
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			jarrayPosts.put(json);
    		}
    	jarray.put(jarrayFav);
    	jarray.put(jarrayPosts);
    	jarray.put(jarrayRep);
    	jarray.put(jarrayFol);
    	jarray.put(myLogin);
    	ctx.close();
    	return jarray;	
    }

    /**
     * pobiera id najstarszego posta w aplikacji,token uzytkownika i id kategorii wedlug ktorej maja byc wyswietlane posty
     * */
    @POST
    @Path("news3Days2")
    @Consumes("application/json")
    public void get3DaysNews(JSONObject inputJsonObj) throws Exception {
    	String stringId = (String) inputJsonObj.get("id");
    	if(stringId.equals("")){
    		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    		PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    		id = postDAO.getMaxId();
    		ctx.close();
    	} else {
    		id = Integer.parseInt(stringId);
    	}
    	token = (String) inputJsonObj.get("token");
    }
    
    /**
     * metoda odpowiedzialna za pobieranie z bazy postow wedlug śledzonych przez użytkownika autorów
     * */
    @GET
    @Path("newsFollowed")
    @Produces("application/json")
    public JSONArray returnFollowedNews(){
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	ReportedDAO reportDAO = (ReportedDAO) ctx.getBean("ReportedDAO", ReportedDAO.class);
    	FollowedDAO followDAO = (FollowedDAO) ctx.getBean("FollowedDAO", FollowedDAO.class);
    	FavouritesDAO favDAO = (FavouritesDAO) ctx.getBean("FavouritesDAO", FavouritesDAO.class);
    	JSONArray jarray = new JSONArray();
    	JSONArray jarrayPosts = new JSONArray();
    	JSONArray jarrayFav = new JSONArray();
    	JSONArray jarrayRep = new JSONArray();
    	JSONArray jarrayFol = new JSONArray();
    	User u = UsersDAO.getByToken(token);
    	String myLogin = u.getLogin();
    	List<Followed> fol = followDAO.getByUserId(u.getId());
    	for(int j=0; j<fol.size(); j++){
			JSONObject json = new JSONObject();
			Followed foli = fol.get(j);
			User us = UsersDAO.getById(foli.getFollowedUserId());
			try {
    			json.put("folUserName",us.getLogin());
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFol.put(json);
			
		}
    	List<Reported> rep = reportDAO.getReportByUserId(u.getId());
    	for(int j=0; j<rep.size(); j++){
			JSONObject json = new JSONObject();
			Reported repi = rep.get(j);
			try {
				json.put("repUserId",Integer.toString(repi.getUserId()));
    			json.put("repPostId",Integer.toString(repi.getPostId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayRep.put(json);
			
		}
    	List<Favourites> fav = favDAO.getByUserId(u.getId());
		for(int j=0; j<fav.size(); j++){
			JSONObject json = new JSONObject();
			Favourites favi = fav.get(j);
			try {
				json.put("favUserId",Integer.toString(favi.getUserId()));
    			json.put("favCategoryId",Integer.toString(favi.getCategoryId()));
			}catch(Exception e){
    			e.printStackTrace();
			}	
			jarrayFav.put(json);
			
		}
    		obj[ja.length()] = id;
    		List<Post> posts = postDAO.getPostByFolIds(obj);
    		for(int i=0; i<posts.size(); i++){
    			Post post = posts.get(i);
    			JSONObject json = new JSONObject();
    			try{
    				//databasePhoto = null;
    				//databasePhoto = post.getPhoto();
    				//SavePhoto sf = new SavePhoto("photo"+post.getPostId());
    				//databasePhoto = sf.readFromFile();
    				//photo = encodeImage(databasePhoto);
    				json.put("postId",post.getPostId());
    				json.put("userLogin",post.getUserLogin());
    				json.put("content",post.getContent());
    				json.put("photo", post.getPhoto());
    				json.put("categoryId", post.getCategory());
    				json.put("addTime",post.getAddTime());
    				json.put("place",post.getPlace());
    				json.put("eventTime", post.getEventTime());
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			jarrayPosts.put(json);
    		}
    	jarray.put(jarrayFav);
    	jarray.put(jarrayPosts);
    	jarray.put(jarrayRep);
    	jarray.put(jarrayFol);
    	jarray.put(myLogin);
    	ctx.close();
    	return jarray;	
    }

    /**
     * pobiera id najstarszego posta w aplikacji,token uzytkownika i liste loginów autorów, których posty mają być wyświetlane w aplikacji
     * */
    @POST
    @Path("newsFollowed2")
    @Consumes("application/json")
    public void getFollowedNews(JSONArray inputJsonObj) throws Exception {
    	String stringId = (String) inputJsonObj.get(0);
    	if(stringId.equals("")){
    		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    		PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    		id = postDAO.getMaxId();
    		ctx.close();
    	} else {
    		id = Integer.parseInt(stringId);
    	}
    	token = (String) inputJsonObj.get(1);
    	ja = inputJsonObj.getJSONArray(2);
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	String[] names = new String[ja.length()];
    	for(int i=0; i< ja.length(); i++){
    		names[i] = ja.getString(i);
    	}
    	User[] users = new User[names.length];
    	for(int i=0; i<names.length; i++){
    		users[i] = UsersDAO.getByName(names[i]);
    	}
    	obj = new Object[users.length+1];
    	for(int i=0; i<users.length; i++){
    		obj[i] = users[i].getId();
    	}
    }
    
    /**
     * metoda obsluguje dodawanie posta do bazy danych
     * */
    @GET
    @Path("post")
    @Produces(MediaType.TEXT_PLAIN)
    public String addPost(){
    	System.out.println(content);
    	databasePhoto = decodeImage(photo);
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	try{
    		PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    		UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    		User user = UsersDAO.getByToken(token);
    		Post post = new Post();
    		//int photoNumber = postDAO.getMaxId() + 1;
    		//SavePhoto sf = new SavePhoto(databasePhoto, "photo"+photoNumber);
			//sf.saveToFile();
    		post.setUserId(user.getId());
    		post.setContent(content);
    		//post.setPhoto(photoUrl+photoNumber+".jfif");
    		post.setCategory(tag);
    		post.setAddTime(addTime);
    		post.setPlace(place);
    		post.setEventTime(eventTime);
    		postDAO.savePost(post);
    		int photoNumber = postDAO.getMaxId();
    		SavePhoto sf = new SavePhoto(databasePhoto, "photo"+photoNumber);
			sf.saveToFile();
			postDAO.updatePhoto(photoNumber, photoUrl+photoNumber+".jfif");
    		response = "TRUE";
    	} catch(Exception e){
    		e.printStackTrace();
    	}
        ctx.close();
        return response;
    }

    /**
     * odbiera dane potrzebne do dodania posta do bazy danych
     * */
    @POST
    @Path("post2")
    @Consumes("application/json")
    public void getPostInfo(JSONObject inputJsonObj) throws Exception {
  	    token = (String) inputJsonObj.get("token");
  	  	content = (String) inputJsonObj.get("content");
  	  System.out.println(content);
  	  	photo = (String) inputJsonObj.get("photo");
  	  	addTime = (String) inputJsonObj.get("addTime");
  	  	place = (String) inputJsonObj.get("place");
  	  	eventTime = (String) inputJsonObj.get("eventTime");
  	  	tag = Integer.parseInt((String) inputJsonObj.get("tag"));
    }
    
    /**
     * pobiera z bazy danych wszystkie posty danego uzytkownika oraz jego zdjecie
     * @throws IOException 
     * */
    @GET
    @Path("account")
    @Produces("application/json")
    public JSONArray returnUserNews() throws IOException{
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	JSONArray jarray = new JSONArray();
    	JSONArray jarrayPosts = new JSONArray();
    	String login;
    		User u = UsersDAO.getByToken(token);
    		userPhoto = null;
    		//userPhoto = u.getPhoto();
    		SavePhoto sf = new SavePhoto("user"+u.getLogin());
    		userPhoto = sf.readUserFromFile();
    		photou = encodeImage(userPhoto);
    		List<Post> posts = postDAO.getPostByUserId(u.getId());
    		login = u.getLogin();
    		for(int i=0; i<posts.size(); i++){
   				JSONObject json = new JSONObject();
   				Post posti = posts.get(i);
    		//	databasePhoto = null;
    		//	databasePhoto = posti.getPhoto();
    		//	photo = encodeImage(databasePhoto);
    			try {
 				json.put("postId",posti.getPostId());
    			json.put("content",posti.getContent());
    			json.put("photo", posti.getPhoto());
    			json.put("categoryId", posti.getCategory());
    			json.put("addTime",posti.getAddTime());
    			json.put("place",posti.getPlace());
    			json.put("eventTime", posti.getEventTime());
    			}catch(Exception e){
    	    			e.printStackTrace();
    	    	}	
    			jarrayPosts.put(json);
    		}
    		
    		jarray.put(jarrayPosts);
    		jarray.put(login);
    		jarray.put(photou);
    	ctx.close();
    	return jarray;	
    }
    
    /**
     * odbiera token uzytkownika od aplikacji
     * */
    @POST
    @Path("account2")
    @Consumes("application/json")
    public void getUserToken(JSONObject inputJsonObj) throws Exception {
  	    token = (String) inputJsonObj.get("token");
    }
    
    /**
     * metoda pobierajaca i wysyla informacje i posty o konkretnym uzytkowniku do aplikaji  
     * @throws IOException 
     * */
    @GET
    @Path("author")
    @Produces("application/json")
    public JSONArray returnAuthorNews() throws IOException{
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	JSONArray jarray = new JSONArray();
    	JSONArray jarrayPosts = new JSONArray();
    	//String removedPostsCount;
    	User u = UsersDAO.getByName(authorLogin);
    	userPhoto = null;
		//userPhoto = u.getPhoto();
    	SavePhoto sf = new SavePhoto("user"+authorLogin);
		userPhoto = sf.readUserFromFile();
		photou = encodeImage(userPhoto);
    	//removedPostsCount = u.getRemoved();
    	List<Post> posts = postDAO.getPostByUserId(u.getId());
    	for(int i=0; i<posts.size(); i++){
   			JSONObject json = new JSONObject();
   			Post posti = posts.get(i);
    		//databasePhoto = null;
    		//databasePhoto = posti.getPhoto();
    		//photo = encodeImage(databasePhoto);
    		try {
 				json.put("postId",posti.getPostId());
    			json.put("content",posti.getContent());
    			json.put("photo", posti.getPhoto());
    			json.put("categoryId", posti.getCategory());
    			json.put("addTime",posti.getAddTime());
    			json.put("place",posti.getPlace());
    			json.put("eventTime", posti.getEventTime());
    		}catch(Exception e){
    	   		e.printStackTrace();
    	    }	
    		jarrayPosts.put(json);
    	}
    	jarray.put(jarrayPosts);
    	jarray.put(posts.size());
    	jarray.put(photou);
    	ctx.close();
    	return jarray;	
    }
    
    /**
     * odbiera login uzytkownika, o ktorym informacje bedziemy pobierac z bazy
     * */
    @POST
    @Path("author2")
    @Consumes("application/json")
    public void getAuthorLogin(JSONObject inputJsonObj) throws Exception {
  	    authorLogin = (String) inputJsonObj.get("userLogin");
    }
    
    /**
     * aktualizuje dane uzytkownika (zdjecie ulubione tagi)
     * */
    @GET
    @Path("edit")
    @Produces(MediaType.TEXT_PLAIN)
    public String editProfile(){
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	FavouritesDAO favDAO = (FavouritesDAO) ctx.getBean("FavouritesDAO", FavouritesDAO.class);
    	try{
    		User u = UsersDAO.getByToken(token);
    		if(!photo.equals("")){
    			userPhoto = null;
    			userPhoto = decodeImage(photo);
    			SavePhoto sf = new SavePhoto(userPhoto, "user"+u.getLogin());
    			sf.deleteUserPhoto();
    			sf.saveUserToFile();
    		}
    		favDAO.deleteById(u.getId());
    		if(favouritesTags.size()>0){
    			for(int i=0; i<favouritesTags.size(); i++){
    				Favourites favourite = new Favourites();
    				favourite.setUserId(u.getId());
    				favourite.setCategoryId(favouritesTags.get(i));
    				favDAO.saveFavourite(favourite);
    			}
    		}
    		response = "TRUE";
    	}catch(Exception e){
	   		e.printStackTrace();
	    }	
        ctx.close();
        return response;
    }   
    
    /**
     * pobiera token zdjecie i liste ulubionych tagow z aplikacji
     * */
    @POST
    @Path("edit2")
    @Consumes("application/json")
    public void getEditParams(JSONArray inputJsonObj) throws Exception {
    	token = (String) inputJsonObj.getString(0);
    	photo = (String) inputJsonObj.getString(1);
    	JSONArray list = inputJsonObj.getJSONArray(2);
    	favouritesTags = new ArrayList<Integer>();
    	for(int i=0; i<list.length(); i++){
    		favouritesTags.add(list.getInt(i));
    	}
    }
    
    /**
     * pobiera i wysyla do aplikacji liste nadchodzacych wydarzen (ktore sie dopiero wydarza)
     * */
    @GET
    @Path("map")
    @Produces("application/json")
    public JSONArray returnNewsOnMap(){
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	FavouritesDAO favDAO = (FavouritesDAO) ctx.getBean("FavouritesDAO", FavouritesDAO.class);
    	JSONArray jarrayPosts = new JSONArray();
    	User u = UsersDAO.getByToken(token);
    	List<Post> posts = postDAO.getAllPost();
    	for(int i=0; i<posts.size(); i++){
    		Post post = posts.get(i);
    		JSONObject json = new JSONObject();
    			try{
    				//databasePhoto = null;
    				//databasePhoto = post.getPhoto();
    				//photo = encodeImage(databasePhoto);
    				json.put("postId",post.getPostId());
    				json.put("userLogin",post.getUserLogin());
    				json.put("content",post.getContent());
    				json.put("photo", post.getPhoto());
    				json.put("categoryId", post.getCategory());
    				json.put("addTime",post.getAddTime());
    				json.put("place",post.getPlace());
    				json.put("eventTime", post.getEventTime());
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			jarrayPosts.put(json);
    	}
    	ctx.close();
    	return jarrayPosts;	
    }

    /**
     * pobiera token uzytkownika z aplikacji
     * */
    @POST
    @Path("map2")
    @Consumes("application/json")
    public void getNewsOnMap(JSONObject inputJsonObj) throws Exception {
    	token = (String) inputJsonObj.get("token");
    }
    
    /**
     * usuwa z bazy danych post o danym id
     * */
    @GET
    @Path("removePost")
    @Produces(MediaType.TEXT_PLAIN)
    public String removePost(){
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	try{
    		User u = UsersDAO.getByToken(token);
    		if(u != null){ 
    			postDAO.deletePostById(postId);
    			SavePhoto sf = new SavePhoto("photo"+postId);
    			sf.deletePhoto();
    			response = "TRUE";
    		}
    	}catch(Exception e){
	   		e.printStackTrace();
	    }	
        ctx.close();
        return response;
    }   
    
    /**
     * pobiera token i id posta ktory ma byc usuniety z bazy
     * */
    @POST
    @Path("removePost2")
    @Consumes("application/json")
    public void getRemoveParams(JSONObject inputJsonObj) throws Exception {
    	token = (String) inputJsonObj.get("token");
    	postId = (Integer) inputJsonObj.get("postId");
    }
    
    /**
     * odpowiada za obsluge zgloszenia naduzycia postu
     * */
    @GET
    @Path("report")
    @Produces(MediaType.TEXT_PLAIN)
    public String reportPost(){
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	ReportedDAO reportDAO = (ReportedDAO) ctx.getBean("ReportedDAO", ReportedDAO.class);
    	PostDAO postDAO = (PostDAO) ctx.getBean("PostDAO", PostDAO.class);
    	try{
    		User u = UsersDAO.getByToken(token);
    		reportDAO.saveReport(postId, u.getId());
    		postDAO.reportPostById(postId);
    		response = "TRUE";
    	}catch(Exception e){
	   		e.printStackTrace();
	    }	
        ctx.close();
        return response;
    }   
    
    /**
     * odbiera id posta, ktory ma byc zgloszony
     * */
    @POST
    @Path("report2")
    @Consumes("application/json")
    public void getReportPostId(JSONObject inputJsonObj) throws Exception {
    	token = (String) inputJsonObj.get("token");
    	postId = (Integer) inputJsonObj.get("postId");
    }
    
    /**
     * odpowiada za dodanie uzytkownika do listy śledzonych użytkowników
     * */
    @GET
    @Path("Follow")
    @Produces(MediaType.TEXT_PLAIN)
    public String followUser(){
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	FollowedDAO followDAO = (FollowedDAO) ctx.getBean("FollowedDAO", FollowedDAO.class);
    	try{
    		User u = UsersDAO.getByToken(token);
    		User u2 = UsersDAO.getByName(userLogin);
    		followDAO.saveFollow(u.getId(), u2.getId());
    		response = "OK";
    	}catch(Exception e){
	   		e.printStackTrace();
	    }	
        ctx.close();
        return response;
    }   
    
    /**
     * odbiera token użytkownika, który ma śledzić oraz login śledzonego użytkownika
     * */
    @POST
    @Path("Follow2")
    @Consumes("application/json")
    public void getFollows(JSONObject inputJsonObj) throws Exception {
    	token = (String) inputJsonObj.get("token");
    	userLogin = (String) inputJsonObj.get("userLogin");
    }
    
    /**
     * odpowiada za usunięcie uzytkownika z listy śledzonych użytkowników
     * */
    @GET
    @Path("stopFollow")
    @Produces(MediaType.TEXT_PLAIN)
    public String stopFollowUser(){
    	String response = "";
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	UserDAO UsersDAO = (UserDAO) ctx.getBean("UsersDAO", UserDAO.class);
    	FollowedDAO followDAO = (FollowedDAO) ctx.getBean("FollowedDAO", FollowedDAO.class);
    	try{
    		User u = UsersDAO.getByToken(token);
    		User u2 = UsersDAO.getByName(userLogin);
    		followDAO.deleteByUserId(u.getId(), u2.getId());
    		response = "OK";
    	}catch(Exception e){
	   		e.printStackTrace();
	    }	
        ctx.close();
        return response;
    }   
    
    /**
     * odbiera token użytkownika, który ma przestać śledzić oraz login śledzonego użytkownika
     * */
    @POST
    @Path("stopFollow2")
    @Consumes("application/json")
    public void getStopFollows(JSONObject inputJsonObj) throws Exception {
    	token = (String) inputJsonObj.get("token");
    	userLogin = (String) inputJsonObj.get("userLogin");
    }

    /**
     * konwertuje tablice bajtow (zdjecie) na string
     * */
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }
     
    /**
     * konwertuje string na tablice bajtow (zdjecie)
     * */
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }
    
    /**
     * Encrypt data
     * @param secretKey - a secret key used for encryption
     * @param data - data to encrypt
     * @return Encrypted data
     * @throws Exception
     */
     public static String cipher(String secretKey, String data) throws Exception {
     	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
     	KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 128, 128);
     	SecretKey tmp = factory.generateSecret(spec);
     	SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
     	Cipher cipher = Cipher.getInstance(ALGORITHM);
     	cipher.init(Cipher.ENCRYPT_MODE, key);
     	return toHex(cipher.doFinal(data.getBytes()));
     }

     /**
     * Decrypt data
     * @param secretKey - a secret key used for decryption
     * @param data - data to decrypt
     * @return Decrypted data
     * @throws Exception
     */
     public static String decipher(String secretKey, String data) throws Exception {
     	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
     	KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 128, 128);
     	SecretKey tmp = factory.generateSecret(spec);
     	SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
     	Cipher cipher = Cipher.getInstance(ALGORITHM);
     	cipher.init(Cipher.DECRYPT_MODE, key);
     	return new String(cipher.doFinal(toByte(data)));
     }

     // Helper methods

     private static byte[] toByte(String hexString) {
     	int len = hexString.length()/2;
     	byte[] result = new byte[len];
     	for (int i = 0; i < len; i++)
     		result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
     	return result;
     }

     public static String toHex(byte[] stringBytes) {
     	StringBuffer result = new StringBuffer(2*stringBytes.length);
     	for (int i = 0; i < stringBytes.length; i++) {
     		result.append(HEX.charAt((stringBytes[i]>>4)&0x0f)).append(HEX.charAt(stringBytes[i]&0x0f));
     	}
     	return result.toString();
     }

}