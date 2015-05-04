package com.picnews.jdbc.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Followed {
private int follow_id, user_id, followed_user_id;
	
	public int getFollowId() {
		return follow_id;
	}
	
	public void setFollowId(int follow_id) {
		this.follow_id = follow_id;
	}
	
	public int getUserId() {
		return user_id;
	}
	
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	
	public int getFollowedUserId() {
		return followed_user_id;
	}
	
	public void setFollowedUserId(int followed_user_id) {
		this.followed_user_id = followed_user_id;
	}
 

}
