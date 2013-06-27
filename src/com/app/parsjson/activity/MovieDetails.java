package com.app.parsjson.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.parsjson.MovieInfo;
import com.app.parsjson.service.MemoryCacheDownloader;
import com.app.parsjson.service.MovieService;
import com.app.parsjson.service.NoCacheDownloader;
import com.example.parsjson.R;

public class MovieDetails extends SettingsActivity {
	private long id;
	private TextView tvMovieName;
	private TextView tvPopularity;
	private TextView tvRuntime;
	private TextView tvRelease;
	private TextView tvOverview;
	private ImageView ivPoster;
	private RatingBar rbRating;

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
			ivPoster = ((ImageView) findViewById(R.id.Poster));
			rbRating = ((RatingBar) findViewById(R.id.movieRate));

			tvPopularity.setText(intent.getStringExtra(MovieList.POPULARITY));
			tvMovieName.setText(intent.getStringExtra(MovieList.NAME));

			GetMovie movieLoader = new GetMovie();
			movieLoader.execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	private class GetMovie extends AsyncTask<Void, Void, MovieInfo> {

		@Override
		protected MovieInfo doInBackground(Void... arg0) {
			MovieService service = new MemoryCacheDownloader(getApplicationContext(), moviesCount);
			return service.getMovie(id);
		}

		@Override
		protected void onPostExecute(MovieInfo results) {
			tvRelease.setText(results.getDate());
			tvRuntime.setText(String.valueOf(results.getRuntime()) + " min");
			tvOverview.setText(results.getOverview());
			rbRating.setRating(results.getRating());
			ivPoster.setImageBitmap(results.getBmp());
		}
	}

}
