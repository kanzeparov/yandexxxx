package com.example.franck.myapplication.network;

import com.example.franck.myapplication.models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/jsonData/moviesData.txt")
    Call<MovieModel> getMyJSON();
}
