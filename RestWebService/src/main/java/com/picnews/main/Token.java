package com.picnews.main;

public class Token {
	
	private char[] token = new char[100];
	
	public Token(){}
	
	String getToken(){
	    for (int i = 0; i < 100; i++){
	      token[i] = (char) (((int)(Math.random() * 26)) + (int)'A');
	    }
	    return (new String(token, 0, 100));
	}
}
