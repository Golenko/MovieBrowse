package com.app.parsjson.service;

import android.content.Context;
import android.graphics.Bitmap;

public class NoCacheDownloader extends AbstractDownloader{

	public NoCacheDownloader(Context context, int moviesCount) {
		super(context, moviesCount);
	}

	@Override
	protected boolean validCache(Long id) {
		return false;
	}

	@Override
	protected Bitmap getFromCache(Long id) {
		return null;
	}

	@Override
	protected void saveToCache(Bitmap bmp, Long id) {
	}

}
