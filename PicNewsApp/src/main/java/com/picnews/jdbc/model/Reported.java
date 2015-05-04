package com.picnews.jdbc.model;
import javax.xml.bind.annotation.XmlRootElement;

public class Reported {
	private Integer userId, postId;
		
	public int getUserId(){
		return userId;
	} 
	
	public void setUserId(int userId){
		this.userId = userId;
	}
	
	public void setPostId(int postId){
		this.postId = postId;
	}
	
	public int getPostId(){
		return postId;
	}

}
