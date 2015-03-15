package com.picnews.jdbc.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Faculty {
	private Integer id;
	private String facultyName, coordinates;
		
	public int getFacultyId(){
		return id;
	} 
	
	public void setFacultyId(int id){
		this.id = id;
	}
	
	public void setFacultyName(String facultyName){
		this.facultyName = facultyName;
	}
	
	public String getFacultyName(){
		return facultyName;
	}
	
	public void setCoordinates(String coordinates){
		this.coordinates = coordinates;
	}
	
	public String getCoordinates(){
		return coordinates;
	}
	
	@Override
	public String toString(){
		return "{ID= " + id + " , Faculty name = " + facultyName + " , Coordinates = " + coordinates + "}";
	}
}