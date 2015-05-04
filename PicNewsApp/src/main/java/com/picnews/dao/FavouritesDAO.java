package com.picnews.dao;

import java.util.List;
import com.picnews.jdbc.model.Favourites;

/*
 * interface odpowiedzialny za operacje na tabeli favourites
 * */
//CRUD operations
public interface FavouritesDAO {
	
	//Create
	//dodaje wpis do tabeli favourites
	public void saveFavourite(Favourites favourite);
	//Read
	//pobiera liste ulubionych tagow dla danego uzytkownika po jego id
	public List<Favourites> getByUserId(int id);
	//Delete
	//usuwa wszystkie wpisy dla danego uzytkownika
	public void deleteById(int id);
}
