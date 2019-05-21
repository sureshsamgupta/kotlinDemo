package com.suresh.movieslistpopular.api

import com.suresh.movieslistpopular.data.MoviesListData

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET(APIConstants.MoviesApi.movieApiName)
    fun getPopularMoviesList(@Query(APIConstants.MoviesApi.apiKey) accessToken: String,
                             @Query(APIConstants.MoviesApi.page) page: Int): Call<MoviesListData>

}

