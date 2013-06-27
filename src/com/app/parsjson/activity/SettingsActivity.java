package com.app.parsjson.activity;

import java.io.File;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.app.parsjson.service.FileSystemDownloader;
import com.app.parsjson.service.MemoryDownloader;
import com.app.parsjson.service.MovieService;
import com.app.parsjson.service.SimpleDownloader;
import com.example.parsjson.R;

public class SettingsActivity extends Activity {
	protected File cacheDir;
	protected int moviesCount;
	protected static MovieService service;
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.action_settings) { // or equals for Object type
			startActivity(new Intent(this, Preference.class));
			return true;
			}
		return false;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		moviesCount = Integer.parseInt(sharedPref.getString("moviesCount", "10"));
		
		String language = sharedPref.getString("language", "en");
		Locale locale;
		if ("Ukrainian".equals(language)) {
			locale = new Locale("ua");
		} else {
			locale = new Locale("en");
		}
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		
		Boolean cacheEnabled = sharedPref.getBoolean("cacheEnabled", true);
		if (cacheEnabled) {
			if (getResources().getString(R.string.settings_memory).equals(sharedPref.getString("cache_location", getResources().getString(R.string.settings_memory)))) {
				service = new MemoryDownloader(getApplicationContext(), moviesCount);
			} else {
				service = new FileSystemDownloader(getApplicationContext(), moviesCount);
			}
		} else {
			service = new SimpleDownloader(getApplicationContext(), moviesCount);
		}
		
		cacheDir = getCacheDir();
//		cacheDir = "@string/settings_card".equals(cacheLocation) ? getCacheDir() : getExternalCacheDir();
		System.out.println(cacheDir.getPath());
	}

}
