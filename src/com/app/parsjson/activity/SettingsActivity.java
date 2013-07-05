package com.app.parsjson.activity;

import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.app.parsjson.service.MovieService;
import com.app.parsjson.service.ServiceFactory;
import com.example.parsjson.R;

public class SettingsActivity extends FragmentActivity {
    protected static MovieService service;
    private SharedPreferences sharedPref;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, Preference.class));
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Locale locale = new Locale(sharedPref.getString("language", "en"));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources()
                        .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        service = ServiceFactory.createService(sharedPref, getApplicationContext());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!Locale.getDefault().toString().equals(sharedPref.getString("language", "en"))) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
