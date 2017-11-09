package com.example.android.quakereport;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
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

        final Earthquake currentItem = getItem(position);

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

        // Format the magnitude properly
        DecimalFormat formatter = new DecimalFormat("0.0");
        String magnitude = formatter.format(currentItem.getMagnitude());

        // Change the colour of the circles
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) (composedView.findViewById(R.id.magnitude)).getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentItem.getMagnitude());

        // Set the color on the magnitude circle
        // Calculate the actual colour value from the given resource ID.
        magnitudeCircle.setColor(ContextCompat.getColor(getContext(), magnitudeColor));

        ((TextView) composedView.findViewById(R.id.magnitude)).setText(magnitude);
        ((TextView) composedView.findViewById(R.id.location)).setText(location);
        ((TextView) composedView.findViewById(R.id.offset)).setText(offset);
        ((TextView) composedView.findViewById(R.id.date)).setText(dateToDisplay);
        ((TextView) composedView.findViewById(R.id.time)).setText(timeToDisplay);

        // Link to the corresponding website
        composedView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, currentItem.getUrl());

                // If it can be resolved an a programme on the phone can open it, open
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(intent);
                }
            }

        });

        return composedView;
    }

    private int getMagnitudeColor(double magnitude) {
        int convertedMagnitude = (int) magnitude;
        int colour = -1;

        switch (convertedMagnitude) {
            case 0:
            case 1:
            case 2:
                colour = R.color.magnitude1;
                break;
            case 3:
                colour = R.color.magnitude2;
                break;
            case 4:
                colour = R.color.magnitude3;
                break;
            case 5:
                colour = R.color.magnitude4;
                break;
            case 6:
                colour = R.color.magnitude5;
                break;
            case 7:
                colour = R.color.magnitude6;
                break;
            case 8:
                colour = R.color.magnitude7;
                break;
            case 9:
                colour = R.color.magnitude8;
                break;
            case 10:
                colour = R.color.magnitude9;
                break;
            default:
                colour = R.color.magnitude10plus;
                break;
        }

        return colour;
    }
}
