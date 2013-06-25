package com.app.parsjson;

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
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Downloader {
	private final static int PORT = 8080;
	private final static String HOST_NAME = "proxy.softservecom.com";
	public static JSONObject GetJson(String link) {

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


}
