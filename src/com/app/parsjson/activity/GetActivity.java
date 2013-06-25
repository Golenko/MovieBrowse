package com.app.parsjson.activity;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.parsjson.Downloader;
import com.app.parsjson.MovieInfo;
import com.app.parsjson.MovieView;
import com.example.parsjson.R;

public class GetActivity extends Activity {
	public final static String M_ID = "ID";
	public final static String NAME = "NAME";
	public final static String POPULARITY = "POPULARITY";
	public String query;
	private LinearLayout framesContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get);
		framesContainer = (LinearLayout) findViewById(R.id.mainLayout);
		
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String name = intent.getStringExtra(SearchManager.QUERY);
			query = "http://private-8a74b-themoviedb.apiary.io/3/search/movie?query=" + URLEncoder.encode(name) + "&api_key=9abbb583ac624dedefae66bfb579e008";
//			query = "http://private-8a74b-themoviedb.apiary.io/3/movie/popular?api_key=9abbb583ac624dedefae66bfb579e008";
		} else {
			query = "http://private-8a74b-themoviedb.apiary.io/3/movie/popular?api_key=9abbb583ac624dedefae66bfb579e008";
		}
		
		BrowseMovies listLoader = new BrowseMovies();
		listLoader.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.get, menu);
		return true;
	}

	private class BrowseMovies extends
			AsyncTask<Void, Integer, ArrayList<MovieInfo>> {

		@Override
		protected ArrayList<MovieInfo> doInBackground(Void... arg0) {
			ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();

			try {
				Downloader download = new Downloader();
				JSONObject entries = download.GetJson(query);
				JSONArray results = entries.getJSONArray("results");
				int count = results.length() < 10 ? results.length() : 10;
				for (int i = 0; i < count; i++) {
					JSONObject filmJSON = results.getJSONObject(i);
					MovieInfo movie = new MovieInfo();
					movie.setName(filmJSON.getString("original_title"));
					movie.setRating((float) filmJSON.getDouble("vote_average") / 2);
					movie.setPoularity((float) filmJSON.getDouble("popularity"));
					movie.setDate(filmJSON.getString("release_date"));
					String url = "https://d3gtl9l2a4fn1j.cloudfront.net/t/p/w185"
							+ filmJSON.getString("poster_path");
					movie.setId(filmJSON.getLong("id"));
					movie.setBmp(download.getImage(url, movie.getId(),
							getCacheDir()));
					list.add(movie);
					publishProgress(i, count);
				}
				return list;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(ArrayList<MovieInfo> results) {
			for (MovieInfo movie : results) {
				MovieView mv = new MovieView(getApplicationContext());
				mv.getInfoFromEntity(movie);
				final long id = movie.getId();
				final String name = movie.getName();
				final float popularity = movie.getPoularity();
				mv.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(GetActivity.this,
								MovieDetails.class);
						intent.putExtra(M_ID, id);
						intent.putExtra(NAME, name);
						intent.putExtra(POPULARITY, Math.round(popularity)
								+ "%");
						startActivity(intent);
					}
				});
				framesContainer.addView(mv);
			}
			((ProgressBar) findViewById(R.id.progressBar2))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.Downloading))
					.setVisibility(View.GONE);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar2);
			progress.setMax(values[1]);
			progress.setProgress(values[0]);
		}
	}

}
