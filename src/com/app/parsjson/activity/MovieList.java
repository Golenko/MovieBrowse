package com.app.parsjson.activity;

import java.util.List;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.parsjson.MovieInfo;
import com.app.parsjson.MovieView;
import com.app.parsjson.service.MemoryCacheDownloader;
import com.app.parsjson.service.MovieService;
import com.app.parsjson.service.NoCacheDownloader;
import com.example.parsjson.R;

public class MovieList extends SettingsActivity {
	public final static String M_ID = "ID";
	public final static String NAME = "NAME";
	public final static String POPULARITY = "POPULARITY";
	private LinearLayout framesContainer;
	private TextView tvInfo;
	private ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get);

		tvInfo = ((TextView) findViewById(R.id.Downloading));
		progress = (ProgressBar) findViewById(R.id.progressBar2);
		framesContainer = (LinearLayout) findViewById(R.id.mainLayout);

		BrowseMovies listLoader = new BrowseMovies(getIntent());
		listLoader.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;

	}

	private class BrowseMovies extends AsyncTask<Void, Void, List<MovieInfo>> {

        private final MovieService service = new MemoryCacheDownloader(getApplicationContext(), moviesCount);
        private final Intent intent;

        public BrowseMovies(final Intent intent) {

            this.intent = intent;
        }

        @Override
		protected List<MovieInfo> doInBackground(Void... arg0) {

            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String name = intent.getStringExtra(SearchManager.QUERY);

                return service.searchMovie(name);

            } else {

			    return service.getMovieList();
            }




		}

		@Override
		protected void onPostExecute(List<MovieInfo> results) {
			for (MovieInfo movie : results) {
				MovieView movieView = new MovieView(getApplicationContext());
				movieView.getInfoFromEntity(movie);

				final long id = movie.getId();
				final String name = movie.getName();
				final float popularity = movie.getPoularity();
				movieView.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MovieList.this, MovieDetails.class);
						intent.putExtra(M_ID, id);
						intent.putExtra(NAME, name);
						intent.putExtra(POPULARITY, Math.round(popularity)
								+ "%");
						startActivity(intent);
					}
				});
				framesContainer.addView(movieView);
			}
			progress.setVisibility(View.GONE);
			tvInfo.setVisibility(View.GONE);
		}

	}

}
