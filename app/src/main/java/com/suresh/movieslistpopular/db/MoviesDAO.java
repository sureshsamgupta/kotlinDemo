package com.suresh.movieslistpopular.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.suresh.movieslistpopular.data.MoviesListData;
import com.suresh.movieslistpopular.utils.Constants;

import java.util.List;


@Dao
public interface MoviesDAO {
    @Insert
    public void insert(MoviesListData.Result... contacts);

    @Update
    public void update(MoviesListData.Result... contacts);

    @Delete
    public void delete(MoviesListData.Result contact);

    @Query("SELECT * FROM "+ Constants.movieData)
    public List<MoviesListData.Result> getContacts();

//    @Query("SELECT * FROM "+Constants.movieData+" WHERE phoneNumber = :number")
//    public MoviesListData.Result getContactWithId(String number);
}