package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

import org.w3c.dom.Text;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;


    private class ViewHolder{

        TextView date;
        TextView forecast;
        TextView highView;
        TextView lowView;
        ImageView iconView;

        private ViewHolder(View view){

            date = (TextView) view.findViewById(R.id.list_item_date_textview);
             forecast = (TextView) view.findViewById(R.id.list_item_forecast_textview);
             highView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);

        }


    }
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * Copy/paste note: Replace existing newView() method in ForecastAdapter with this one.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        // TODO: Determine layoutId from viewType

        switch (viewType){
            case VIEW_TYPE_TODAY: layoutId = R.layout.list_item_forecast_today;
                break;
            case VIEW_TYPE_FUTURE_DAY: layoutId = R.layout.list_item_forecast;
                break;
            default: layoutId = -1;
                break;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }



    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        //Get the ViewHolder object
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);

        // Use placeholder image for now
        // Don't need this anymore ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        viewHolder.iconView.setImageResource(R.drawable.ic_launcher);

        //We don't need this anymore
        //Get references to TextViews
//        TextView date = (TextView) view.findViewById(R.id.list_item_date_textview);
//        TextView forecast = (TextView) view.findViewById(R.id.list_item_forecast_textview);
//        TextView highView = (TextView) view.findViewById(R.id.list_item_high_textview);
//        TextView lowView = (TextView) view.findViewById(R.id.list_item_low_textview);

        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);

        //TextView highView = (TextView) view.findViewById(R.id.list_item_high_textview);
        viewHolder.highView.setText(Utility.formatTemperature(context, high, isMetric));
        viewHolder.lowView.setText(Utility.formatTemperature(context, low, isMetric));

        //Get correct date format and update TextView
        String formattedDate = Utility.getFriendlyDayString(context, cursor.getLong(cursor.getColumnIndex(
                WeatherContract.WeatherEntry.COLUMN_DATE)));
        viewHolder.date.setText(formattedDate);


        viewHolder.forecast.setText(cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC)));


    }
}