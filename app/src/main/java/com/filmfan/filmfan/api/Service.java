package com.filmfan.filmfan.api;

import com.filmfan.filmfan.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {
    @GET("/movie/now_playing")
    Call<MoviesResponse>getNowPlayingMovies(@Query("api_key") String apiKey);
}
