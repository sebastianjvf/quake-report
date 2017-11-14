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
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    /**
     * Constant value for the earthquake loader ID.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /**
     * List adapter to be cleared on Loader removal.
     */
    EarthquakeListAdapter adapter;

    /**
     * Progress bar indicator, should show when loading.
     */
    ProgressBar progressBarIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Find the progress bar indicator
        progressBarIndicator = (ProgressBar) findViewById(R.id.loading);

        if (!isConnected) {
            TextView noConnection = ((TextView) findViewById(R.id.no_connection));
            noConnection.setText(R.string.no_connection);
            noConnection.setVisibility(View.VISIBLE);
            progressBarIndicator.setVisibility(View.GONE);
        } else {
            // Set empty screen
            ListView earthquakeListView = (ListView) findViewById(R.id.list);
            TextView emptyView = ((TextView) findViewById(R.id.empty));
            earthquakeListView.setEmptyView(emptyView);

            progressBarIndicator.setVisibility(View.VISIBLE);
            adapter = new EarthquakeListAdapter(EarthquakeActivity.this, new ArrayList<Earthquake>());

            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
        }
    }

    /**
     * Returns a new AsyncLoader, passing in the url
     *
     * @param i
     * @param bundle
     * @return
     */
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

        // Load the preferences which were written to the preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        // Create a new loader for the given URL
        return new EarthquakeAsyncLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        progressBarIndicator.setVisibility(View.GONE);

        // Create (and reset) a new {@link ArrayAdapter} of earthquakes
        adapter.clear();

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        TextView emptyView = ((TextView) findViewById(R.id.empty));
        emptyView.setText(R.string.no_earthquakes);

        // Check if no earthquakes were loaded
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Executes a HTTP request in a background threat to load earthquake data using a loader.
     * Takes a URL as input parameter and outputs a list of earthquake objects
     */
    private static class EarthquakeAsyncLoader extends AsyncTaskLoader<List<Earthquake>> {

        String url;

        public EarthquakeAsyncLoader(Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        public List<Earthquake> loadInBackground() {
            ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(url);
            return earthquakes;
        }
    }
}
