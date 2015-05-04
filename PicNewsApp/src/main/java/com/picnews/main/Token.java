package com.picnews.main;

/*
 * klasa odpowiedzialna za generowanie tokenu 
 * */
public class Token {
	
	private char[] token = new char[10];
	
	public Token(){}
	
	//metoda generuje token
	String getToken(){
	    for (int i = 0; i < 10; i++){
	      token[i] = (char) (((int)(Math.random() * 26)) + (int)'A');
	    }
	    return (new String(token, 0, 10));
	}
}
