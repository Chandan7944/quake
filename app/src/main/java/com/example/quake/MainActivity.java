/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.quake;

import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import java.util.List;

@android.support.annotation.RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<details>> {

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=15";
    private customadapter adapter;
    private TextView emptyView;
    private ProgressBar circle;

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                //this, android.R.layout.simple_list_item_1, earthquakes);
        adapter=new customadapter(this,new ArrayList<details>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        circle = (ProgressBar) findViewById(R.id.loading_spinner);

        emptyView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                details obj = adapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                assert obj != null;
                intent.putExtra(SearchManager.QUERY,obj.get_url());
                startActivity(intent);
            }
        });
        if(networkInfo!=null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        }else {
            circle.setVisibility(View.GONE);
            emptyView.setText("No internet connection :(");
        }

    }


    @Override
    public Loader<List<details>> onCreateLoader(int i, Bundle bundle) {
            // Create a new loader for the given URL
            //EarthQuakeLoader obj = new EarthQuakeLoader(this,USGS_REQUEST_URL);
            return new EarthQuakeLoader(this,USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<details>> loader, List<details> data) {

        circle.setVisibility(View.GONE);
        emptyView.setText("No earthquakes found.");
        adapter.clear();
        if(data != null && !data.isEmpty()){
            adapter.addAll((ArrayList<details>)data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<details>> loader) {
        adapter.clear();
    }
}

