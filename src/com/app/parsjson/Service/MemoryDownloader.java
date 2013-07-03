package com.app.parsjson.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.example.parsjson.R;

class MemoryDownloader extends SimpleDownloader {
	private static final Map<Long, Bitmap> cache = new HashMap<Long, Bitmap>();

	public MemoryDownloader(Context context, int moviesCount) {
		super(context, moviesCount);
	}

	protected boolean validCache(Long id) {
		return cache.containsKey(id);
	}

	protected Bitmap getFromCache(Long id) {
		System.out.println("MAP        FIND        MAP");
		return cache.get(id);
	}

	protected void saveToCache(Long id, Bitmap bmp) {
		cache.put(id, bmp);

	}

	@Override
	protected Bitmap getImage(Long id, String url) { 
		if (url == null)
			return ((BitmapDrawable) context.getResources().getDrawable(
					R.drawable.no_image)).getBitmap();

		if (validCache(id)) {
			return getFromCache(id);
		} else {
			Bitmap image = BitmapFactory.decodeStream(getInputStream(url));
			saveToCache(id, image);
			return image;
		}
	}
}
