package com.suresh.movieslistpopular.api;

import android.content.Context;

import com.suresh.movieslistpopular.data.MoviesListData;

import retrofit2.Call;
import retrofit2.Callback;

public class WebServiceRequests {
    private static WebServiceRequests instance;
    private ApiInterface apiInterface;


    private WebServiceRequests() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public static WebServiceRequests getInstance() {
        if (instance == null)
            instance = new WebServiceRequests();
        return instance;
    }


    public void getPopularMoviesList(int page, Callback<MoviesListData> callback) {

        Call<MoviesListData> call = apiInterface.getPopularMoviesList(APIConstants.API_KEY, page);
        call.enqueue(callback);
    }


}
