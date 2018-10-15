package com.filmfan.filmfan.utils;

import com.filmfan.filmfan.BuildConfig;

public class UrlManager {
    public static final String BASE_URL="https://api.themoviedb.org/3/";
    public static final String API_KEY=BuildConfig.MOVIE_DB_API_TOKEN;
    public static final String NOW_PLAYING_MOVIES=BASE_URL+"movie/now_playing?api_key="+BuildConfig.MOVIE_DB_API_TOKEN;
    private String movie_detial;
    private String movie_actor;
    private int movie_id;
    private String request_token="authentication/token/new?api_key=";
    private String Create_session="authentication/session/new?api_key=";
    private String Request_token_access="https://www.themoviedb.org/authenticate/";
    private String Guest_Session_id="authentication/guest_session/new?api_key=";
    private String ratingQuery="/rating?api_key=";
    private String guestSessQuery="&guest_session_id=";
    public UrlManager() {
    }

    public UrlManager(int movie_id) {
        this.movie_id = movie_id;
    }

    public void setMovie_detial(int movieId){
        this.movie_id=movieId;
    }

    public String getMovie_detial(){
        return BASE_URL+"movie/"+movie_id+"?api_key="+API_KEY;
    }
    public String getMovie_credits(){
        return BASE_URL+"movie/"+movie_id+"/credits?api_key="+API_KEY;
    }
    public String CreateRequestToken(){
        return BASE_URL+request_token+API_KEY;
    }

    public String RequestTokenAccess(String token_provided){
        return Request_token_access+token_provided;
    }

    public String CreateGuestSessionId(){
        return BASE_URL+Guest_Session_id+API_KEY;
    }

    public String RatingRequest(String GuestSessId){
        return BASE_URL+"movie/"+movie_id+ratingQuery+API_KEY+guestSessQuery+GuestSessId;
    }

}
