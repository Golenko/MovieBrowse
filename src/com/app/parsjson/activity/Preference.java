package com.app.parsjson.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.parsjson.R;

public class Preference extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    private final PreferenceFragment generalFragment = new GeneralFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, generalFragment);
        generalFragment.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        transaction.commit();
        finish();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("language".equals(key)) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    public static class GeneralFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }
}
