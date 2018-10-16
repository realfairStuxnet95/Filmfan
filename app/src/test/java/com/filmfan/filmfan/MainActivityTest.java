package com.filmfan.filmfan;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    @Mock
    MainActivity activity;

    @Before
    public void setUp() throws Exception{
        activity=new MainActivity();
        activity.onCreate(null);
    }

    @Test
    public void openMovieDetail() throws Exception{
        // Arrage

        // Act

        // Assert
    }
}
