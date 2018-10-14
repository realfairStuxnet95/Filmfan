package com.filmfan.filmfan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.filmfan.filmfan.api.Client;
import com.filmfan.filmfan.api.Service;
import com.filmfan.filmfan.model.Movie;
import com.filmfan.filmfan.model.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //Widgets
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    //variables
    private Context context;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        //initialize Widgets
        swipeRefreshLayout=findViewById(R.id.swipeTorefresh);
        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        loadMovies();
    }

    //Activity instances
    public Activity getActivity(){
        Context context=this;
        while(context instanceof ContextWrapper){
            if(context instanceof ContextWrapper){
                return (Activity) context;
            }
            context=((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
    //widgets initialization
    private void initViews(){
        recyclerView=findViewById(R.id.recycle_view);
        if(getActivity().getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
    //Movies Request Loading Method
    private void loadMovies(){
        try{
            if(BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()){
                displayToast(getResources().getString(R.string.Invalid_api_token));
            }
            //Instantiate Retrofit Requests
            Client client=new Client();
            Service apiService=client.getClient().create(Service.class);
            Call<MoviesResponse>call=apiService.getNowPlayingMovies(BuildConfig.MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                }
            });
        }catch (Exception e){

        }
    }
    private void nowShowingMovies(){

    }
    private void displayToast(String msg){
        Toast.makeText(context,String.valueOf(msg),Toast.LENGTH_LONG).show();
    }
}
