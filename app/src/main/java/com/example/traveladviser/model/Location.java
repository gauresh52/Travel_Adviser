package com.example.traveladviser.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    // Getters and Setters
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
