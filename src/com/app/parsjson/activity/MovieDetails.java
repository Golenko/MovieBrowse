package com.app.parsjson.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.parsjson.Downloader;
import com.app.parsjson.MovieInfo;
import com.app.parsjson.Resource;
import com.example.parsjson.R;

public class MovieDetails extends Activity {
	private long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mov_det);
		Intent intent = getIntent();
		id = intent.getLongExtra(GetActivity.M_ID, 0);
		setTitle(intent.getStringExtra(GetActivity.NAME));
		((TextView) findViewById(R.id.movieName)).setText(intent.getStringExtra(GetActivity.NAME));
		GetMovie gm = new GetMovie();
		gm.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movie_details, menu);
		return true;
	}

	private class GetMovie extends
			AsyncTask<Void, Void, MovieInfo> {

		public final static String MESSAGE = "ID";

		@Override
		protected MovieInfo doInBackground(Void... arg0) {

			try {
				JSONObject film = Downloader
						.GetJson("http://private-8a74b-themoviedb.apiary.io/3/movie/" + id + "?api_key=9abbb583ac624dedefae66bfb579e008");
				

					MovieInfo mi = new MovieInfo();
					mi.setRating((float) film.getDouble("vote_average") / 2);
					mi.setDate(film.getString("release_date"));
					mi.setRuntime(Integer.parseInt(film.getString("runtime")));
					mi.setPoularity((float) film.getDouble("popularity"));
					mi.setOverview(film.getString("overview"));
					String url = "https://d3gtl9l2a4fn1j.cloudfront.net/t/p/w185"
							+ film.getString("poster_path");
					Resource res = new Resource();
					mi.setBmp(res.getImage(url, id, getCacheDir()));
				return mi;
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(MovieInfo results) {
			((TextView) findViewById(R.id.releaseVal)).setText(results.getDate());
			((TextView) findViewById(R.id.runtimeVal)).setText(String.valueOf(results.getRuntime()) + " min");
			((TextView) findViewById(R.id.overview)).setText(results.getOverview());
			((TextView) findViewById(R.id.popularityVal)).setText(Math.round(results.getPoularity()) + "%");
			((RatingBar) findViewById(R.id.movieRate)).setRating(results.getRating());
			((ImageView) findViewById(R.id.moviePic)).setImageBitmap(results.getBmp());
		}
	}

}
