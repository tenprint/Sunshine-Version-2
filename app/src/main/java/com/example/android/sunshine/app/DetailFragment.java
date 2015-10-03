package com.example.android.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Created by jlipatap on 10/3/15.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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

    //Declare View items
    TextView date;
    TextView forecast;
    TextView highView;
    TextView lowView;
    ImageView iconView;
    TextView humidityView;
    TextView windSpeedView;
    TextView pressureView;

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
        }


        //Get View references
        date = (TextView) rootView.findViewById(R.id.list_item_date_textview);
        forecast = (TextView) rootView.findViewById(R.id.list_item_forecast_textview);
        highView = (TextView) rootView.findViewById(R.id.list_item_high_textview);
        lowView = (TextView) rootView.findViewById(R.id.list_item_low_textview);
        iconView = (ImageView) rootView.findViewById(R.id.list_item_icon);
        humidityView = (TextView) rootView.findViewById(R.id.list_item_humidity);
        windSpeedView = (TextView) rootView.findViewById(R.id.list_item_wind_speed);
        pressureView = (TextView) rootView.findViewById(R.id.list_item_pressure);

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
        if (mShareActionProvider != null) {
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
        switch (id) {

            case URI_LOADER:
                return new CursorLoader(getActivity(),
                        Uri.parse(contentUri),
                        weatherDetailFields,
                        null,
                        null,
                        null);
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        String[] columns = data.getColumnNames();  //Put column names from query into an array

        if (data.moveToFirst()) { //if statement handles cursor without data

            Context context = getActivity();

            //Get correct date format and update TextView
            String formattedDate = Utility.getFriendlyDayString(context, data.getLong(data.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_DATE)));
            date.setText(formattedDate);

            // Read user preference for metric or imperial temperature units
            boolean isMetric = Utility.isMetric(context);

            // Read high temperature from cursor
            double high = data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
            double low = data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));

            highView.setText(Utility.formatTemperature(context, high, isMetric));
            lowView.setText(Utility.formatTemperature(context, low, isMetric));

            //Read and set Forecast, Humidity, Wind, Pressure
            String forecastStr = data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC));
            forecast.setText(forecastStr);

            humidityView.setText("Humidity: " + data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY)));
            windSpeedView.setText("Wind: " + data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED)));
            pressureView.setText("Pressure: " + data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_PRESSURE)));

            //Update Sharable mForecast
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(formattedDate + " " + forecastStr + " " + high + "/" + low);
            mForecastStr = stringBuilder.toString();
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
