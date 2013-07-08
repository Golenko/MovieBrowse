package com.app.parsjson.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.parsjson.MovieInfo;
import com.example.parsjson.R;

public class DetailsFragment extends Fragment {
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

    static final String DETAILS_TAG = "details_fragment";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_mov_det, null);
        Intent intent = getActivity().getIntent();

        
        Bundle extras = intent.getBundleExtra(ListFragment.EXTRAS);
        
        id = (Long) extras.get(ListFragment.M_ID);
        getActivity().setTitle((CharSequence) extras.get(ListFragment.NAME));

        tvMovieName = ((TextView) v.findViewById(R.id.movieName));
        tvPopularity = ((TextView) v.findViewById(R.id.popularityVal));
        tvRuntime = ((TextView) v.findViewById(R.id.runtimeVal));
        tvRelease = ((TextView) v.findViewById(R.id.releaseVal));
        tvOverview = ((TextView) v.findViewById(R.id.overview));
        ivPoster = (ImageView) v.findViewById(R.id.Poster);
        rbRating = (RatingBar) v.findViewById(R.id.movieRate);
        btnTrailer = (Button) v.findViewById(R.id.btnTrailer);
        tvPopularity.setText((CharSequence) extras.get(ListFragment.POPULARITY));
        tvMovieName.setText((CharSequence) extras.get(ListFragment.NAME));
        btnTrailer.setVisibility(View.GONE);
        GetMovie movieLoader = new GetMovie();
        movieLoader.execute();

        return v;
    }

    public void onButtonClick(View view) {
        Log.d("sss", trailer);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer)));

    }

    private class GetMovie extends AsyncTask<Void, Void, MovieInfo> {

        @Override
        protected MovieInfo doInBackground(Void... arg0) {
            return SettingsActivity.service.getMovie(id);
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
