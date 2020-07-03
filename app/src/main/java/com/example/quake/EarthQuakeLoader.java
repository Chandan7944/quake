package com.example.quake;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.List;


public class EarthQuakeLoader extends AsyncTaskLoader<List<details>> {
    private String urls;
    public EarthQuakeLoader(@NonNull Context context,String url) {
        super(context);
        this.urls = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<details> loadInBackground() {
        Log.e("TEST", "Loading started");
        List<details> earthquake = null;
        if (urls.length() < 1) {
            return null;
        }
        try {
            earthquake =  QueryUtils.fetchlist(urls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return earthquake;
    }

    @Override
    public void deliverResult(@Nullable List<details> data) {
        super.deliverResult(data);
    }
}
