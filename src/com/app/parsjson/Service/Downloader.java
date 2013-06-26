package com.app.parsjson.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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

public class Downloader implements MovieService {
	private final static int PORT = 8080;
	private final static String HOST_NAME = "proxy.softservecom.com";
	private Context context;

	public Downloader(Context context) {
		this.context = context;
	}

	private JSONObject GetJson(String link) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost(HOST_NAME, PORT);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(link);
		httpGet.setHeader("Accept", "application/json");
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();

			byte[] buffer = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while (true) {
				int size = is.read(buffer);
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

	private Bitmap getImage(String link, Long id) {
		Bitmap bmp = null;
		/*--- this method downloads an Image from the given URL, 
		 *  then decodes and returns a Bitmap object
		 ---*/
		if (link == null)
			return ((BitmapDrawable) context.getResources().getDrawable(
					R.drawable.sample2)).getBitmap();

		File image = new File(context.getCacheDir(), id + ".jpg");
		if (image.exists()) {
			System.out.println(image.getAbsolutePath() + "   find");
			try {
				FileInputStream fis = new FileInputStream(image);
				bmp = BitmapFactory.decodeStream(fis);
				System.out.println(bmp.getHeight());

			} catch (FileNotFoundException e) {
				e.printStackTrace();

			}

		} else {
			try {
				URL url = new URL(link);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
						HOST_NAME, PORT));
				URLConnection yc = url.openConnection(proxy);
				yc.setDoInput(true);
				yc.connect();
				InputStream input = yc.getInputStream();
				bmp = BitmapFactory.decodeStream(input);

				FileOutputStream fOut = new FileOutputStream(image);
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bmp;
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

	@Override
	public List<MovieInfo> getMovieList(String query, int count) {
		List<MovieInfo> list = new ArrayList<MovieInfo>();

		try {
			JSONObject entries = GetJson(query);
			JSONArray results = entries.getJSONArray("results");
			int resultCount = results.length() < count ? results.length()
					: count;
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
		return null;
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

}