package com.app.parsjson.service;

import android.content.Context;
import android.graphics.Bitmap;

public class NoCacheDownloader extends AbstractDownloader{

	public NoCacheDownloader(Context context, int moviesCount) {
		super(context, moviesCount);
	}

	@Override
	protected boolean validCache(long id) {
		return false;
	}

	@Override
	protected Bitmap getFromCache(long id) {
		return null;
	}

	@Override
	protected void saveToCache(Bitmap bmp, long id) {
	}

}
