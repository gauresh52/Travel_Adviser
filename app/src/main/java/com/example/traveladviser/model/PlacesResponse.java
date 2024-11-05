package com.example.traveladviser.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlacesResponse {
    @SerializedName("results")
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }
}
