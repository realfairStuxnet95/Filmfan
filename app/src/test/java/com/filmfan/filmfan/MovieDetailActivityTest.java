package com.filmfan.filmfan;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MovieDetailActivityTest {
    MovieDetails activity;

    @Before
    public void setUp() throws Exception{
        activity=new MovieDetails();
        activity.onCreate(null);
    }

    @Test
    public void shouldLaunchMovieActivity(){

    }

    @Test
    public void updateMovieOverView(){
        String givenString="test123";
        activity.movie_overview.setText(givenString);

        //Assert
        String actualString=activity.movie_overview.getText().toString();
        assertEquals(givenString,actualString);
    }
}
