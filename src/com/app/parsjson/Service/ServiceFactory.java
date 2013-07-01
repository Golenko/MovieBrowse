package com.app.parsjson.service;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class ServiceFactory {
    private SharedPreferences preferences;
    private Context context;

    public ServiceFactory(SharedPreferences preferences, Context context) {
        this.preferences = preferences;
        this.context = context;
    }

    public MovieService createService(int moviesCount) {
        Boolean cacheEnabled = preferences.getBoolean("cacheEnabled", true);
        if (cacheEnabled) {
            if ("memory".equals(preferences.getString("cache_location", "memory"))) {
                return new MemoryDownloader(context, moviesCount);
            } else {
                return new FileSystemDownloader(context, moviesCount);
            }
        } else {
            return new SimpleDownloader(context, moviesCount);
        }
    }

    public static MovieService createService(SharedPreferences preferences, Context context) {
        int moviesCount = Integer.parseInt(preferences.getString("moviesCount", "10"));

        Boolean cacheEnabled = preferences.getBoolean("cacheEnabled", true);
        if (cacheEnabled) {
            if ("memory".equals(preferences.getString("cache_location", "memory"))) {
                return new MemoryDownloader(context, moviesCount);
            } else {
                return new FileSystemDownloader(context, moviesCount);
            }
        } else {
            return new SimpleDownloader(context, moviesCount);
        }
    }
}
