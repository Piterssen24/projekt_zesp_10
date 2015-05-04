package com.picnews.jdbc.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Tag {
	private Integer categoryId;
	private String tag;
		
	public int getCategoryId(){
		return categoryId;
	} 
	
	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}
	
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public String getTag(){
		return tag;
	}
	
	@Override
	public String toString(){
		return "{Tag ID= " + categoryId + " , Tag = " + tag + "}";
	}
}