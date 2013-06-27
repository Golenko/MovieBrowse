package com.app.parsjson.service;

import java.util.HashMap;

import com.example.parsjson.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class MemoryDownloader extends SimpleDownloader {
	private static final HashMap<Long, Bitmap> cache = new HashMap<Long, Bitmap>();

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

	protected void saveToCache(Bitmap bmp, Long id) {
		cache.put(id, bmp);

	}

	@Override
	protected Bitmap getImage(String url, Long id) {
		if (url == null)
			return ((BitmapDrawable) context.getResources().getDrawable(
					R.drawable.sample2)).getBitmap();

		if (validCache(id)) {
			return getFromCache(id);
		} else {
			Bitmap image = BitmapFactory.decodeStream(getInputStream(url));
			saveToCache(image, id);
			return image;
		}
	}
}
