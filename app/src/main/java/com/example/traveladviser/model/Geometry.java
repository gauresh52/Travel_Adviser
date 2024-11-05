package com.example.traveladviser.model;


import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    private Location location;

    // Getter and Setter
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
