package com.app.parsjson.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.parsjson.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class MemoryCacheDownloader extends AbstractDownloader{

	public MemoryCacheDownloader(Context context, int moviesCount) {
		super(context, moviesCount);
	}

	@Override
	protected boolean validCache(long id) {
		File image = new File(context.getCacheDir(), id + ".jpg");
		return image.exists();
	}

	@Override
	protected void saveToCache(Bitmap bmp, long id) {
		File image = new File(context.getCacheDir(), id + ".jpg");
		try {
			FileOutputStream fOut = new FileOutputStream(image);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Bitmap getFromCache(long id) {
		File image = new File(context.getCacheDir(), id + ".jpg");
		System.out.println(image.getAbsolutePath() + "   find");
		FileInputStream fis;
		try {
			fis = new FileInputStream(image);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
