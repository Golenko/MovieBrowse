package com.app.parsjson.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.app.parsjson.Link;
import com.app.parsjson.MovieInfo;
import com.example.parsjson.R;

public class SimpleDownloader implements MovieService {
	private final static int PORT = 8080;
	private final static String HOST_NAME = "proxy.softservecom.com";
	protected Context context;
	protected final int moviesCount;

	public SimpleDownloader(Context context, final int moviesCount) {
		this.context = context;
		this.moviesCount = moviesCount;
	}

	@Override
	public MovieInfo getMovie(long id) {
		try {
			String query = Link.HOST + "movie/" + id + "?" + Link.APIKEY;
			JSONObject movieJSON = GetJson(query);
			MovieInfo movie = ParseJSON(movieJSON);
			movie.setRuntime(Integer.parseInt(movieJSON.getString("runtime")));
			movie.setOverview(movieJSON.getString("overview"));
			movie.setBmp(getImage(movie.getUrl(), id));
			return movie;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<MovieInfo> getMovieList() {
		String query = Link.HOST + Link.POPULAR + Link.APIKEY;
		return listMovie(query);
	}

	@Override
	public List<MovieInfo> searchMovie(final String name) {
		String query = "";
		try {
			query = Link.HOST + Link.SEARCH_MOVIE
					+ URLEncoder.encode(name, "UTF-8") + "&" + Link.APIKEY;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return listMovie(query);
	}

	protected Bitmap getImage(String url, Long id) {
		if (url == null)
			return ((BitmapDrawable) context.getResources().getDrawable(
					R.drawable.sample2)).getBitmap();
		return BitmapFactory.decodeStream(getInputStream(url));
	}

	protected InputStream getInputStream(String url) {
		InputStream inputStream = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost(HOST_NAME, PORT);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	private List<MovieInfo> listMovie(String query) {
		List<MovieInfo> list = new ArrayList<MovieInfo>();
		try {
			JSONObject entries = GetJson(query);
			JSONArray results = entries.getJSONArray("results");
			int resultCount = Math.min(results.length(), moviesCount);
			for (int i = 0; i < resultCount; i++) {
				JSONObject movieJSON = results.getJSONObject(i);
				MovieInfo movie = ParseJSON(movieJSON);
				movie.setName(movieJSON.getString("original_title"));
				movie.setPoularity((float) movieJSON.getDouble("popularity"));
				movie.setId(movieJSON.getLong("id"));
				movie.setBmp(getImage(movie.getUrl(), movie.getId()));

				list.add(movie);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private JSONObject GetJson(String query) {

		try {
			InputStream inputStream = getInputStream(query);
			byte[] buffer = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while (true) {
				int size = inputStream.read(buffer);
				if (size == -1)
					break;
				sb.append(new String(buffer, 0, size));
			}

			JSONObject entries = new JSONObject(sb.toString());
			return entries;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private MovieInfo ParseJSON(JSONObject movieJSON) throws JSONException {
		MovieInfo movie = new MovieInfo();
		movie.setRating((float) movieJSON.getDouble("vote_average") / 2);
		String url = movieJSON.getString("poster_path").equals("null") ? null
				: Link.IMAGE_HOST + movieJSON.getString("poster_path");
		movie.setUrl(url);
		movie.setDate(movieJSON.getString("release_date"));
		return movie;
	}
}