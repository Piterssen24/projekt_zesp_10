package com.picnews.jdbc.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Favourites {
	private int fav_id, user_id, category_id;
	
	public int getFavId() {
		return fav_id;
	}
	
	public void setFavId(int fav_id) {
		this.fav_id = fav_id;
	}
	
	public int getUserId() {
		return user_id;
	}
	
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	
	public int getCategoryId() {
		return category_id;
	}
	
	public void setCategoryId(int category_id) {
		this.category_id = category_id;
	}
 
	
	@Override
	public String toString(){
		return "{ID= " + fav_id + " , user = " + user_id + " , category= " + category_id + "}";
	}
}

