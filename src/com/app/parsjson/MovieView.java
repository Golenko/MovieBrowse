package com.app.parsjson;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.parsjson.R;

public class MovieView extends RelativeLayout {
    private ImageView moviePic;
    private TextView movieName;
    private TextView popularityVal;
    private TextView releaseVal;
    private RatingBar movieRate;

    public MovieView(Context context) {
        super(context);
        initComponent();
    }
    
    public MovieView(Context context, MovieInfo movInf) {
        super(context);
        initComponent();
        this.setMovieRate(movInf.getRating());
        this.setMovieName(movInf.getName());
        this.setMoviePic(movInf.getBmp());
        this.setMoviePopularity(movInf.getPoularity());
        this.setDate(movInf.getDate());
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.movie_layout, this);
        moviePic = (ImageView) findViewById(R.id.moviePic);
        movieName = (TextView) findViewById(R.id.movieName);
        popularityVal = (TextView) findViewById(R.id.runtimeVal);
        releaseVal = (TextView) findViewById(R.id.releaseVal);
        movieRate = (RatingBar) findViewById(R.id.movieRate);

    }

    public void setMoviePic(Bitmap bmp) {
        moviePic.setImageBitmap(bmp);
    }

    public void setMoviePopularity(float pop) {
        popularityVal.setText(Math.round(pop) + "%");
    }

    public void setMovieName(String name) {
        movieName.setText(name);
    }

    public void setDate(String date) {
        releaseVal.setText(date);
    }

    public void setMovieRate(float rating) {
        movieRate.setRating(rating);
    }

}
