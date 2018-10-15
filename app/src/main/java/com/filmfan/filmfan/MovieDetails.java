package com.filmfan.filmfan;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.filmfan.filmfan.adapters.ActorAdapter;
import com.filmfan.filmfan.model.Actor;
import com.filmfan.filmfan.model.Movie;
import com.filmfan.filmfan.utils.UrlManager;
import com.filmfan.filmfan.utils.Widgets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDetails extends AppCompatActivity {
    private static final String TAG = "MovieDetails";

    //Widgets
    private Toolbar toolbar;
    private ImageView movie_poster;
    private CollapsingToolbarLayout toolbar_layout;
    private TextView movie_overview;
    private TextView txt_release_date;
    private TextView txt_genres;
    private TextView txt_ratings;
    private FloatingActionButton rate_movie;
    private RatingBar rating_bar;
    private Dialog dialog;
    //vars
    Movie movie;
    RequestQueue queue;
    Widgets widgets;
    UrlManager urlManager;
    ArrayList<Actor>actors;
    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private String RequestToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //Initialize Widgets
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get Intent Extra
        if(getIntent().getSerializableExtra("movie")!=null || getIntent().getSerializableExtra("movie")!=""){
            movie= (Movie) getIntent().getSerializableExtra("movie");
            //fill UI with Data
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fillViews(movie);
            }
        }else{
            finish();
        }

    }

    private void initializeWidgets(){
        widgets=new Widgets(MovieDetails.this);
        urlManager=new UrlManager(movie.getId());
        movie_poster=findViewById(R.id.movie_poster);
        toolbar_layout=findViewById(R.id.toolbar_layout);
        movie_overview=findViewById(R.id.movie_overview);
        txt_release_date=findViewById(R.id.txt_release_date);
        txt_genres=findViewById(R.id.txt_genres);
        txt_ratings=findViewById(R.id.txt_ratings);
        rate_movie=findViewById(R.id.rate_movie);
        rating_bar=findViewById(R.id.rating_bar);


        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                double ratings=(ratingBar.getRating())*2;
                CreateRequestToken(ratings);
                widgets.displayToast(String.valueOf(ratings));
            }
        });

        //FloatingActionButton Clicked
        rate_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"AddingToFavorites Fab Clicked");
                AddingToFavorites(view);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fillViews(Movie movie){
        initializeWidgets();
        //Load online Poster with Glide
        Glide.with(MovieDetails.this)
                .load(movie.getPoster_path())
                .into(movie_poster);

        toolbar_layout.setExpandedTitleColor(getColor(R.color.white));
        toolbar_layout.setCollapsedTitleTextColor(getColor(R.color.white));
        toolbar_layout.setTitle(movie.getOriginal_title());
        movie_overview.setText(movie.getOverview());
        txt_release_date.setText(formatStringset(getString(R.string.release),movie.getRelease_date()));
        txt_ratings.setText(formatStringset(getResources().getString(R.string.ratings),String.valueOf(movie.getVote_average())));

        requestMovieInfo(movie);
        RequestActors();
    }

    //String Well Formatting
    private StringBuilder formatStringset(String resource,String value){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(resource);
        stringBuilder.append(value);

        return stringBuilder;
    }
    private void requestMovieInfo(Movie movie){
        String BACKLOG_URL=urlManager.getMovie_detial();
        //Check RequestQue
        if(queue==null){
            // Instantiate the RequestQueue.
            queue = Volley.newRequestQueue(this);
        }
        //Check for Api Token
        if(BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()){
            widgets.displayToast(getResources().getString(R.string.Invalid_api_token));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BACKLOG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        FilterGenres(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void FilterGenres(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray genres=jsonObject.getJSONArray(getResources().getString(R.string.genres));
            for(int position=0;position<genres.length();position++){
                JSONObject Genre=genres.getJSONObject(position);
                String genre_name=Genre.getString(getResources().getString(R.string.genre_name));
               txt_genres.append(genre_name+" / ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void RequestActors(){
        //Check for Api Token
        if(BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()){
            widgets.displayToast(getResources().getString(R.string.Invalid_api_token));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlManager.getMovie_credits(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getActors(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void getActors(String response){
        Log.d(TAG, "initImageBitmaps: preparing Json Parsing.");
        try {
            JSONObject responseActors=new JSONObject(response);
            JSONArray ActorsArray=responseActors.getJSONArray(getResources().getString(R.string.cast));
            for(int position=0;position<ActorsArray.length();position++){
                JSONObject actorObject=ActorsArray.getJSONObject(position);
                int cast_id=actorObject.getInt(getResources().getString(R.string.cast_id));
                String character=actorObject.getString(getResources().getString(R.string.character));
                String credit_id=actorObject.getString(getResources().getString(R.string.credit_id));
                int gender=actorObject.getInt(getResources().getString(R.string.gender));
                int id=actorObject.getInt(getResources().getString(R.string.cast_id));
                String name=actorObject.getString(getResources().getString(R.string.name));
                int order=actorObject.getInt(getResources().getString(R.string.order));
                String profile_path=actorObject.getString(getResources().getString(R.string.profile_path));

                //Attach Returned Response to Actor Model
                //Actor actor=new Actor(cast_id,character,credit_id,gender,id,name,order,profile_path);
                mNames.add(name+"/"+character);
                mImageUrls.add("https://image.tmdb.org/t/p/w500"+profile_path);
                //Append Actor Object to Actor List
                //actors.add(actor);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initRecyclerView();

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        ActorAdapter adapter = new ActorAdapter(MovieDetails.this, mNames,mImageUrls);
        recyclerView.setAdapter(adapter);
    }

    //Create Request Token
    private void CreateRequestToken(final Double ratings){
        //Check for Api Token
        if(BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()){
            widgets.displayToast(getResources().getString(R.string.Invalid_api_token));
        }
        RatingDialog(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlManager.CreateGuestSessionId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            Boolean status=jsonResponse.getBoolean("success");
                            if(status){
                                RequestToken=jsonResponse.getString("guest_session_id");
                                if(dialog.isShowing()){
                                    dialog.dismiss();
                                }
                                RateMovie(urlManager.RatingRequest(RequestToken), ratings);
                            }else{
                                widgets.displayToast(getResources().getString(R.string.request_token_failed));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void RateMovie(String ratingUrl,Double rating){
        final JSONObject ratingJson=new JSONObject();
        try {
            ratingJson.put("value",rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ratingUrl,ratingJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        widgets.displayToast(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json;charset=utf-8");
                return headers;
            }
        };;

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
    private void OpenBrowser(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    private void RatingDialog(boolean status){
        dialog= new  Dialog(MovieDetails.this);
        dialog.setContentView(R.layout.rate_layout);
        dialog.setCancelable(false);
        if(status){
            dialog.show();
        }else{
            dialog.dismiss();
        }
    }

    //Adding to favorite
    private void AddingToFavorites(View view){
        Snackbar snackbar = Snackbar
                .make(view, getResources().getString(R.string.favorites_alert), Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
