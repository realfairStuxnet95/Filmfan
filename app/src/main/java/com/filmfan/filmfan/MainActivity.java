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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.filmfan.filmfan.adapters.MovieAdapter;

import com.filmfan.filmfan.model.Movie;
import com.filmfan.filmfan.utils.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //Widgets
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    //variables
    private Context context;
    private List<Movie>movies;
    RequestQueue queue;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        //initialize Widgets
        progressBar=findViewById(R.id.progressBar);
        swipeRefreshLayout=findViewById(R.id.swipeTorefresh);
        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
            }
        });

        initViews();
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
        Log.d(TAG, "initViews Called.");

        recyclerView=findViewById(R.id.recycle_view);
        movies=new ArrayList<>();
        //Grant different grid size
        if(getActivity().getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,4));
        }
        adapter=new MovieAdapter(context,movies);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        nowShowingMovies();
    }
    //Movies Request Loading Method
    private void nowShowingMovies(){
        Log.d(TAG, "nowShowingMovies Called.");
        //Check RequestQue
        if(queue==null){
            // Instantiate the RequestQueue.
           queue = Volley.newRequestQueue(this);
        }
        //Check for Api Token
        if(BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()){
            displayToast(getResources().getString(R.string.Invalid_api_token));
        }
        showProgress(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlManager.NOW_PLAYING_MOVIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        //Response Returned
                        Log.d(TAG,response);
                        showProgress(false);
                        parseJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayToast(error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    //progressBar switch method
    private void showProgress(boolean status){
        if(status)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }
    private void displayToast(String msg){
        Toast.makeText(context,String.valueOf(msg),Toast.LENGTH_LONG).show();
    }
    //parsing Returned result
    private void parseJson(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray(getResources().getString(R.string.result_json));
            for(int i=0;i<jsonArray.length();i++){
                JSONObject movie_result=jsonArray.getJSONObject(i);
                int voteCount=movie_result.getInt(getResources().getString(R.string.vote_count));
                int id=movie_result.getInt(getResources().getString(R.string.id));;
                boolean video=movie_result.getBoolean(getResources().getString(R.string.video));
                double vote_average=movie_result.getDouble(getResources().getString(R.string.vote_average));
                String title=movie_result.getString(getResources().getString(R.string.title));
                double popularity=movie_result.getDouble(getResources().getString(R.string.popularity));
                String poster_path=movie_result.getString(getResources().getString(R.string.poster_path));
                String original_language=movie_result.getString(getResources().getString(R.string.original_language));
                String original_title=movie_result.getString(getResources().getString(R.string.original_title));
                String backdrop_path=movie_result.getString(getResources().getString(R.string.backdrop_path));
                boolean adult=movie_result.getBoolean(getResources().getString(R.string.adult));
                String overview=movie_result.getString(getResources().getString(R.string.overview_json));
                String release_date=movie_result.getString(getResources().getString(R.string.release_date));

                //Append Resultset to Movie Model
                Movie movie=new Movie(voteCount,id,video,vote_average,title,popularity,poster_path,original_language,original_title,backdrop_path,adult,overview,release_date);
                movies.add(movie);
            }
            //notify adapter for view data changes
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
