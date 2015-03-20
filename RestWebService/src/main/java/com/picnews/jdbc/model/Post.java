package com.picnews.jdbc.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Post {
	private int postId;
	private int userId;
	private String content;
	private byte[] photo;
	private String postType;
	private String addTime;
	private int category;
	private String place;
	private String eventTime;
	private String userLogin;
	
	public int getPostId(){
		return postId;
	} 
	
	public void setPostId(int postId){
		this.postId = postId;
	}
	
	public String getUserLogin(){
		return userLogin;
	} 
	
	public void setUserLogin(String userLogin){
		this.userLogin = userLogin;
	}
	
	public int getUserId(){
		return userId;
	} 
	
	public void setUserId(int userId){
		this.userId = userId;
	}
	
	public String getContent(){
		return content;
	} 
	
	public void setContent(String content){
		this.content = content;
	}
	
	public byte[] getPhoto(){
		return photo;
	} 
	
	public void setPhoto(byte[] photo){
		this.photo = photo;
	}
	
	public String getPostType(){
		return postType;
	} 
	
	public void setPostType(String postType){
		this.postType = postType;
	}
	
	public int getCategory(){
		return category;
	} 
	
	public void setCategory(int category){
		this.category = category;
	}
	
	public void setAddTime(String addTime){
		this.addTime = addTime;		
	}
	
	public String getAddTime(){
		return addTime;
	}
	
	public void setPlace(String place){
		this.place = place;
	}

	public String getPlace(){
		return place;		
	}

	public void setEventTime(String eventTime){
		this.eventTime = eventTime;
	}

	public String getEventTime(){
		return eventTime;		
	}
	
	@Override
	public String toString(){
		return "{Post id=" + postId + ",User_id=" + userId + ",Post content=" + content + "Photo: " + photo 
				+ "Category: " + category + "Added time: " + addTime + "Place: " + place + "Event time: " + eventTime +"}";
	}
	
}

