package com.app.parsjson.service;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;

public class MapCacheDownloader extends AbstractDownloader{
	private HashMap <Long, Bitmap> cache;
	public MapCacheDownloader(Context context, int moviesCount) {
		super(context, moviesCount);
	}

	@Override
	protected boolean validCache(Long id) {
		return cache.containsKey(id);
	}

	@Override
	protected Bitmap getFromCache(Long id) {
		return cache.get(id);
	}

	@Override
	protected void saveToCache(Bitmap bmp, Long id) {
		cache.put(id, bmp);
		
	}

}
