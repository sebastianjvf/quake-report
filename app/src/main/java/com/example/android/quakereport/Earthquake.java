package com.example.android.quakereport;

public class Earthquake {

    private String location;
    private long time;
    private double magnitude;

    public Earthquake(String location, long time, double magnitude) {
        this.location = location;
        this.time = time;
        this.magnitude = magnitude;
    }

    public String getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }

    public double getMagnitude() {
        return magnitude;
    }
}