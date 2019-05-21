package com.suresh.movieslistpopular.api;

public interface APIConstants {

    //    String BASE_URL = "http://13.58.237.150:8002/";
    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_URL = "http://image.tmdb.org/t/p/w500/";
    String API_KEY = "68288a242fe0f72cc7c50662b1a0fd1a";

    interface MoviesApi {

        String movieApiName = "movie/popular";
        String apiKey = "api_key";
        String page = "page";

    }


}
