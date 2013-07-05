package com.app.parsjson.activity;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.app.parsjson.MovieInfo;
import com.example.parsjson.R;

public class MovieList extends SettingsActivity {
    public final static String M_ID = "ID";
    public final static String NAME = "NAME";
    public final static String POPULARITY = "POPULARITY";

    final String ATTRIBUTE_TEXT_POPULARITY = "popularity";
    final String ATTRIBUTE_TEXT_RELEASE = "release";
    final String ATTRIBUTE_TEXT_NAME = "name";
    final String ATTRIBUTE_PICTURE = "image";
    final String ATTRIBUTE_RATING = "rating";
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;

    private TextView tvInfo;
    private ProgressBar progress;
    private ListView lvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        tvInfo = ((TextView) findViewById(R.id.Downloading));
        progress = (ProgressBar) findViewById(R.id.progressBar2);
        lvMovies = (ListView) findViewById(R.id.listView1);

        BrowseMovies listLoader = new BrowseMovies(getIntent());
        listLoader.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;

    }

    private class BrowseMovies extends AsyncTask<Void, Void, List<MovieInfo>> {
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
        protected void onPostExecute(List<MovieInfo> result) {
            final List<MovieInfo> results = result;
            String[] from = { ATTRIBUTE_TEXT_RELEASE, ATTRIBUTE_TEXT_POPULARITY, ATTRIBUTE_TEXT_NAME,
                    ATTRIBUTE_PICTURE, ATTRIBUTE_RATING };
            int[] to = { R.id.releaseVal, R.id.runtimeVal, R.id.movieName, R.id.moviePic, R.id.movieRate };

            data = new ArrayList<Map<String, Object>>();
            for (MovieInfo movie : results) {
                m = new HashMap<String, Object>();
                m.put(ATTRIBUTE_TEXT_RELEASE, movie.getDate());
                m.put(ATTRIBUTE_TEXT_POPULARITY, Math.round(movie.getPoularity()) + "%");
                m.put(ATTRIBUTE_TEXT_NAME, movie.getName());
                m.put(ATTRIBUTE_PICTURE, movie.getBmp());
                m.put(ATTRIBUTE_RATING, movie.getRating());
                data.add(m);
            }
            SimpleAdapter sAdapter = new SimpleAdapter(getApplicationContext(), data, R.layout.movie_layout, from, to);
            sAdapter.setViewBinder(new MyViewBinder());
            lvMovies.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("rrrrrrrrr", String.valueOf(results.get((int) id).getId()));
                    Intent intent = new Intent(MovieList.this, MovieDetails.class);
                    intent.putExtra(M_ID, results.get((int) id).getId());
                    intent.putExtra(NAME, results.get((int) id).getName());
                    intent.putExtra(POPULARITY, Math.round(results.get((int) id).getPoularity()) + "%");
                    startActivity(intent);
                }
            });

            lvMovies.setAdapter(sAdapter);
            progress.setVisibility(View.GONE);
            tvInfo.setVisibility(View.GONE);
            lvMovies.setVisibility(View.VISIBLE);
        }

    }

    class MyViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            switch (view.getId()) {
            case R.id.movieRate:
                ((RatingBar) view).setRating((Float) data);
                return true;
            case R.id.moviePic:
                ((ImageView) view).setImageBitmap((Bitmap) data);
                return true;
            }
            return false;
        }

    }
}
