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

        if(composedView == null) {
            composedView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Earthquake currentItem = getItem(position);

        // Parse to a readable format
        Date date = new Date(currentItem.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String dateToDisplay = dateFormat.format(date);


        ((TextView) composedView.findViewById(R.id.magnitude)).setText(currentItem.getMagnitude() + "");
        ((TextView) composedView.findViewById(R.id.location)).setText(currentItem.getLocation());
        ((TextView) composedView.findViewById(R.id.time)).setText(dateToDisplay);

        return composedView;
    }
}
