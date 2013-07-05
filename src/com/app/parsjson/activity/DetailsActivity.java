package com.app.parsjson.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.parsjson.R;

public class DetailsActivity extends SettingsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            finish();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_details);
        }
    }
}
