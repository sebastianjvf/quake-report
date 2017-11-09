package com.example.android.quakereport;

public class Earthquake {

    private String location;
    private long time;
    private double magnitude;
    private String url;

    public Earthquake(String location, long time, double magnitude, String url) {
        this.location = location;
        this.time = time;
        this.magnitude = magnitude;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}