package com.filmfan.filmfan.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.filmfan.filmfan.MovieDetails;
import com.filmfan.filmfan.R;
import com.filmfan.filmfan.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private static final String TAG = "MovieAdapter";
    //variables
    private Context mContext;
    private List<Movie>movieList;

    //initialize constructor
    public MovieAdapter(Context context,List<Movie>movieList){
        this.mContext=context;
        this.movieList=movieList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card,viewGroup,false);

        return new MyViewHolder(view,mContext,movieList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        String vote=Double.toString(movieList.get(i).getVote_average());
        //attachment data source to widget set.
        viewHolder.movie_title.setText(movieList.get(i).getTitle());
        viewHolder.vote_average.setText(vote);
        viewHolder.release_date.setText(movieList.get(i).getRelease_date());

        Glide.with(mContext)
                .load(movieList.get(i).getPoster_path())
                .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    //ViewHolder static class
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //widgets
        public ImageView thumbnail;
        public TextView movie_title;
        public TextView release_date;
        public TextView vote_average;

        //vars
        private Context context;
        private List<Movie>movies;
        public MyViewHolder(@NonNull View itemView, final Context context, final List<Movie>movies) {
            super(itemView);

            this.context=context;
            this.movies=movies;
            //initialize widgets
            thumbnail=itemView.findViewById(R.id.thumbnail);
            movie_title=itemView.findViewById(R.id.movie_title);
            release_date=itemView.findViewById(R.id.release_date);
            vote_average=itemView.findViewById(R.id.vote_average);

            //view  onCLickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "setOnClickListener: called.");
                    //get Position
                    int position=getAdapterPosition();
                    Movie movie=movies.get(position);
                    PrepareIntent(context,movie);
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
        private void PrepareIntent(Context context,Movie movie){
            Intent intent=new Intent(context,MovieDetails.class);
            intent.putExtra("movie",movie);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
