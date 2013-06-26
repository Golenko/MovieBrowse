package com.app.parsjson.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.parsjson.R;

public class SettingsActivity extends Activity {
	protected File cacheDir;
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, PrefActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String cacheLocation = sharedPref.getString("moviesCount", "@string/settings_memory");
		cacheDir = getCacheDir();
//		cacheDir = "@string/settings_card".equals(cacheLocation) ? getCacheDir() : getExternalCacheDir();
		System.out.println(cacheDir.getPath());
	}

}
