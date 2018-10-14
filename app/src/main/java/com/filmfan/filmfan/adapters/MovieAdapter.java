package com.filmfan.filmfan.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.filmfan.filmfan.R;
import com.filmfan.filmfan.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
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

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
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
        return 0;
    }

    //ViewHolder static class
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //widgets
        public ImageView thumbnail;
        public TextView movie_title;
        public TextView release_date;
        public TextView vote_average;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //initialize widgets
            thumbnail=itemView.findViewById(R.id.thumbnail);
            movie_title=itemView.findViewById(R.id.movie_title);
            release_date=itemView.findViewById(R.id.release_date);
            vote_average=itemView.findViewById(R.id.vote_average);
        }
    }
}
