package com.filmfan.filmfan.views;

import com.filmfan.filmfan.interfaces.MainActivityView;

public class MainActivityPresenter {
    MainActivityView view;

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }

    public void MovieActivityLaunched(Class activity){
        view.launchMovieDetails(activity);
    }
}
