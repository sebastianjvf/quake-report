package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeListAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeListAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, R.layout.list_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View composedView = convertView;

        if (composedView == null) {
            composedView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Earthquake currentItem = getItem(position);

        // Parse to a readable format
        Date date = new Date(currentItem.getTime());
        String dateToDisplay = (new SimpleDateFormat("dd MMM yyyy")).format(date);
        String timeToDisplay = (new SimpleDateFormat("H.m")).format(date);

        // Separate string into location and offset
        String location = "";
        String offset = "";

        // Check if it includes of
        if (currentItem.getLocation().indexOf("of") > 0) {
            offset = currentItem.getLocation().substring(0, currentItem.getLocation().indexOf("of") + 2);
            location = currentItem.getLocation().substring(currentItem.getLocation().indexOf("of") + 3);
        } else {
            offset = "Near the";
            location = currentItem.getLocation();
        }

        ((TextView) composedView.findViewById(R.id.magnitude)).setText(currentItem.getMagnitude() + "");
        ((TextView) composedView.findViewById(R.id.location)).setText(location);
        ((TextView) composedView.findViewById(R.id.offset)).setText(offset);
        ((TextView) composedView.findViewById(R.id.date)).setText(dateToDisplay);
        ((TextView) composedView.findViewById(R.id.time)).setText(timeToDisplay);

        return composedView;
    }
}
