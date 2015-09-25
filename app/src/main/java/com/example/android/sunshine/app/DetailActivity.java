/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.example.android.sunshine.app;

//import android.app.LoaderManager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.android.sunshine.app.data.WeatherContract;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
        private static final int URI_LOADER = 0;
        private String mForecastStr;
        private String contentUri;
        private String[] weatherDetailFields = {
                WeatherContract.LocationEntry.COLUMN_CITY_NAME,
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
                WeatherContract.WeatherEntry.COLUMN_DEGREES
        };

        public DetailFragment() {
            setHasOptionsMenu(true);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // The detail Activity called via intent.  Inspect the intent for forecast data.
            Intent intent = getActivity().getIntent();

            if (intent != null) {
                contentUri = intent.getDataString();

                getLoaderManager().initLoader(URI_LOADER, null, this);
                //mForecastStr =
            }

            if (null != mForecastStr) {
                ((TextView) rootView.findViewById(R.id.detail_text))
                        .setText(mForecastStr);
            }

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);

            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set/change the share intent.
            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            // Attach an intent to this ShareActionProvider.  You can update this at any time,
            // like when the user selects a new piece of data they might like to share.
            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }

        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    mForecastStr + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }

        @Override
        public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch (id){

                case URI_LOADER:
                    return new CursorLoader(getActivity(),
                        Uri.parse(contentUri),
                        weatherDetailFields,
                        null,
                        null,
                        null);
                default: return null;
            }

        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

            /*
             *   This version uses a Projection to get only needed fields in the query
             *  -if statement handles a blank cursor
             *  -uses StringBuilder class to append cursor data as it's iterated
             *  -pushes data to mForecast string which does two things:
             *       #1 Populates TextView
             *       #2 Makes data available for Sharing
             */

            String[] columns = data.getColumnNames();  //Put column names from query into an array
            TextView textView = ((TextView) getView().findViewById(R.id.detail_text)); //Reference to TextView
            StringBuilder stringBuilder = new StringBuilder(); //Class used to append Cursor data to String

            if(data.moveToFirst()) { //if statement handles cursor without data

                //Iterate through cursor.  Display column name + column data
                for (int i = 0; i < columns.length; i++) {
//                textView.append(columns[i] + ": "+data.getString(i) + "\n");
                    stringBuilder.append(columns[i] + ": " + data.getString(i) + "\n");
                }

                //Update mForecastStr. It is used for Sharing
                mForecastStr = stringBuilder.toString();

                //Update TextView
                textView.setText(mForecastStr);
            } else textView.setText("NO DATA"); //Default message to display in case of no data from cursor


            //This works but it's hard coded, let's try with a Projection
//            textView.append("\nCity Name: " + data.getString(data.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME)));
//            textView.append("\nDate: "+data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE)));
//            textView.append("\nOutlook: "+data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC)));
//            textView.append("\nHigh/Low: "+data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP))
//                +"/"+data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP)));
//            textView.append("\nHumidity: "+data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY)));
            //Pressure
            //Wind
            //Degrees

        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

        }


    }
}
