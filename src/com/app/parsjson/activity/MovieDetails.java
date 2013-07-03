package com.app.parsjson.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.parsjson.MovieInfo;
import com.example.parsjson.R;

public class MovieDetails extends SettingsActivity {
    private long id;
    private String trailer;
    private TextView tvMovieName;
    private TextView tvPopularity;
    private TextView tvRuntime;
    private TextView tvRelease;
    private TextView tvOverview;
    private ImageView ivPoster;
    private RatingBar rbRating;
    private Button btnTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            finish();
            intent.setClass(MovieDetails.this, MovieList.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_mov_det);
            id = intent.getLongExtra(MovieList.M_ID, 0);
            setTitle(intent.getStringExtra(MovieList.NAME));

            tvMovieName = ((TextView) findViewById(R.id.movieName));
            tvPopularity = ((TextView) findViewById(R.id.popularityVal));
            tvRuntime = ((TextView) findViewById(R.id.runtimeVal));
            tvRelease = ((TextView) findViewById(R.id.releaseVal));
            tvOverview = ((TextView) findViewById(R.id.overview));
            ivPoster = (ImageView) findViewById(R.id.Poster);
            rbRating = (RatingBar) findViewById(R.id.movieRate);
            btnTrailer = (Button) findViewById(R.id.btnTrailer);
            tvPopularity.setText(intent.getStringExtra(MovieList.POPULARITY));
            tvMovieName.setText(intent.getStringExtra(MovieList.NAME));
            btnTrailer.setVisibility(View.GONE);
            GetMovie movieLoader = new GetMovie();
            movieLoader.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public void onButtonClick(View view) {
        Log.d("sss", trailer);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer)));


    }

    private class GetMovie extends AsyncTask<Void, Void, MovieInfo> {

        @Override
        protected MovieInfo doInBackground(Void... arg0) {
            return service.getMovie(id);
        }

        @Override
        protected void onPostExecute(MovieInfo results) {
            tvRelease.setText(results.getDate());
            tvRuntime.setText(String.valueOf(results.getRuntime()) + " min");
            tvOverview.setText(results.getOverview());
            rbRating.setRating(results.getRating());
            ivPoster.setImageBitmap(results.getBmp());
            if (results.getTrailer() != null) {
                trailer = results.getTrailer();
                btnTrailer.setVisibility(View.VISIBLE);
            }
        }
    }

}
