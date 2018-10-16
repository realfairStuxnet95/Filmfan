package com.filmfan.filmfan.views;

import com.filmfan.filmfan.interfaces.MovieDetailsView;

public class MovieDetailsPresenter {
    MovieDetailsView view;

    public MovieDetailsPresenter(MovieDetailsView view) {
        this.view = view;
    }

    public void movieOverviewUpdated(String text){
        view.changeMovieOverView(text);
    }
}
