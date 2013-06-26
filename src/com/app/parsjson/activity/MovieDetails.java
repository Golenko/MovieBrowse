package com.app.parsjson.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.parsjson.MovieInfo;
import com.app.parsjson.Service.Downloader;
import com.app.parsjson.Service.MovieService;
import com.example.parsjson.R;

public class MovieDetails extends SettingsActivity {
	private long id;

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
			((TextView) findViewById(R.id.movieName)).setText(intent
					.getStringExtra(MovieList.NAME));
			((TextView) findViewById(R.id.popularityVal)).setText(intent
					.getStringExtra(MovieList.POPULARITY));
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
			MovieService service = new Downloader(getCacheDir());
			return service.getMovie(id);

		}

		protected void onPostExecute(MovieInfo results) {
			((TextView) findViewById(R.id.releaseVal)).setText(results
					.getDate());
			((TextView) findViewById(R.id.runtimeVal)).setText(String
					.valueOf(results.getRuntime()) + " min");
			((TextView) findViewById(R.id.overview)).setText(results
					.getOverview());
			((RatingBar) findViewById(R.id.movieRate)).setRating(results
					.getRating());
			((ImageView) findViewById(R.id.Poster)).setImageBitmap(results
					.getBmp());
		}
	}

}
