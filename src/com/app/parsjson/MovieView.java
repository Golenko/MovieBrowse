package com.app.parsjson;

import com.example.parsjson.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MovieView extends RelativeLayout {
	private ImageView moviePic;
	private TextView movieName;
	private TextView popularity;
	private TextView popularityVal;
	private TextView release;
	private TextView rating;
	private TextView releaseVal;
	private RatingBar movieRate;
	
	
	public MovieView(Context context) {
		super(context);
		initComponent();
	}

	private void initComponent() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.movie_layout, this);
		moviePic = (ImageView) findViewById(R.id.moviePic);
		movieName = (TextView) findViewById(R.id.movieName);
		popularity = (TextView) findViewById(R.id.popularity);
		popularityVal = (TextView) findViewById(R.id.runtimeVal);
		release = (TextView) findViewById(R.id.release);
		releaseVal = (TextView) findViewById(R.id.releaseVal);
		rating = (TextView) findViewById(R.id.rating);
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
	
	public void getInfoFromEntity (MovieInfo movInf) {
		this.setMovieRate(movInf.getRating());
		this.setMovieName(movInf.getName());
		this.setMoviePic(movInf.getBmp());
		this.setMoviePopularity(movInf.getPoularity());
		this.setDate(movInf.getDate());
	}

}
