package com.app.parsjson.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.example.parsjson.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

class FileSystemDownloader extends SimpleDownloader {
	public FileSystemDownloader(Context context, int moviesCount) {
		super(context, moviesCount);
	}

	protected boolean validCache(Long id) {
		return new File(context.getCacheDir(), id + ".jpg").exists();
	}

	protected void saveToCache(Long id, Bitmap bmp) {
		File image = new File(context.getCacheDir(), id + ".jpg");
		try {
			FileOutputStream fOut = new FileOutputStream(image);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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

	protected Bitmap getFromCache(Long id) {
		File image = new File(context.getCacheDir(), id + ".jpg");
		System.out.println(image.getAbsolutePath() + "   find");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return BitmapFactory.decodeStream(fis);
	}

}
