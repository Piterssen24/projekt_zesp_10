package com.picnews.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

public class SavePhoto {
	byte[] data;
	String name;
	
	public SavePhoto(byte[] value, String name){
		this.data = value;
		this.name = name;
	}
	
	public SavePhoto(String name){
		this.name = name;
	}
	
	
	public void saveToFile() throws IOException, URISyntaxException{	
			Path path = Paths.get(".").toAbsolutePath();
			//FileOutputStream out = new FileOutputStream(path.getParent().getParent().getParent()+"/photos/"+name+".jfif");
			FileOutputStream out = new FileOutputStream("/progzesp/pz2014/zesp10/photos/"+name+".jfif");
	    	 out.write(data);
	    	 out.close();
	}
	
	public void saveUserToFile() throws IOException, URISyntaxException{	
		Path path = Paths.get(".").toAbsolutePath();
		//FileOutputStream out = new FileOutputStream(path.getParent().getParent().getParent()+"/photos/"+name+".jfif");
		FileOutputStream out = new FileOutputStream("/progzesp/pz2014/zesp10/userPhotos/"+name+".jfif");
    	 out.write(data);
    	 out.close();
}
	
	public byte[] readFromFile() throws IOException{
		Path path = Paths.get(".").toAbsolutePath();
		//File fi = new File(path.getParent().getParent().getParent()+"/photos/"+name+".jfif");
		File fi = new File("/progzesp/pz2014/zesp10/photos/"+name+".jfif");
		byte[] fileContent = Files.readAllBytes(fi.toPath());
		return fileContent;
	}
	
	public byte[] readUserFromFile() throws IOException{
		Path path = Paths.get(".").toAbsolutePath();
		//File fi = new File(path.getParent().getParent().getParent()+"/photos/"+name+".jfif");
		File fi = new File("/progzesp/pz2014/zesp10/userPhotos/"+name+".jfif");
		byte[] fileContent = Files.readAllBytes(fi.toPath());
		return fileContent;
	}
	
	public void deletePhoto(){
		Path path = Paths.get(".").toAbsolutePath();
		//File fi = new File(path.getParent().getParent().getParent()+"/photos/"+name+".jfif");
		File fi = new File("/progzesp/pz2014/zesp10/photos/"+name+".jfif");
		fi.delete();
	}
	
	public void deleteUserPhoto(){
		Path path = Paths.get(".").toAbsolutePath();
		//File fi = new File(path.getParent().getParent().getParent()+"/photos/"+name+".jfif");
		File fi = new File("/progzesp/pz2014/zesp10/userPhotos/"+name+".jfif");
		fi.delete();
	}
}
