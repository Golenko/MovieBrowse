package com.app.parsjson.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.example.parsjson.R;

public class PrefActivity extends PreferenceActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
   	 getFragmentManager().beginTransaction()
     .replace(android.R.id.content, new PrefsFragment())
     .commit();
		super.onCreate(savedInstanceState);

	}
	
	
	public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

   

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }


}
