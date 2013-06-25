package com.app.parsjson.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import com.app.parsjson.Resource;
import com.example.parsjson.R;

public class GetActivity extends Activity {
	public final static String M_ID = "ID";
	public final static String NAME = "NAME";
	private LinearLayout framesContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get);
		setTitle("Movie browser");
		framesContainer = (LinearLayout) findViewById(R.id.mainLayout);
		// Выполняем асинхронный гет-запрос
		BrowseMovies bm = new BrowseMovies();
		bm.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.get, menu);
		return true;
	}

	// Создаём асинхронный класс, котрый подключится к интернету и выполнит
	// запрос
	private class BrowseMovies extends
			AsyncTask<Void, Integer, ArrayList<MovieInfo>> {

		@Override
		protected ArrayList<MovieInfo> doInBackground(Void... arg0) {
			ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();

			try {
				JSONObject entries = Downloader.GetJson("http://private-8a74b-themoviedb.apiary.io/3/movie/popular?api_key=9abbb583ac624dedefae66bfb579e008");
				JSONArray results = entries.getJSONArray("results");

				for (int i = 0; i < 10; i++) {
					JSONObject film = results.getJSONObject(i);
					MovieInfo mi = new MovieInfo();
					mi.setName(film.getString("original_title"));
					mi.setRating((float) film.getDouble("vote_average") / 2);
					mi.setPoularity((float) film.getDouble("popularity"));
					mi.setDate(film.getString("release_date"));
					String url = "https://d3gtl9l2a4fn1j.cloudfront.net/t/p/w185"
							+ film.getString("poster_path");
					mi.setId(film.getLong("id"));
					Resource resource = new Resource();
					mi.setBmp(resource.getImage(url, mi.getId(), getCacheDir()));
					list.add(mi);
					publishProgress(i, 10);
				}
				return list;
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(ArrayList<MovieInfo> results) {
			for (MovieInfo mi : results) {
				MovieView mv = new MovieView(getApplicationContext());
				mv.getInfoFromEntity(mi);
				final long id = mi.getId();
				final String name = mi.getName();
				mv.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(GetActivity.this, MovieDetails.class);
						intent.putExtra(M_ID, id);
						intent.putExtra(NAME, name);
						startActivity(intent);
					}
				});
				framesContainer.addView(mv);
			}
			((ProgressBar) findViewById(R.id.progressBar2)).setVisibility(View.GONE);
			((TextView)findViewById(R.id.Downloading)).setVisibility(View.GONE);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);
			pb.setMax(values[1]);
			pb.setProgress(values[0]);
		}
	}

}
