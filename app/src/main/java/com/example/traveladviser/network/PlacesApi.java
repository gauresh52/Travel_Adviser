package com.example.traveladviser.network;

import com.example.traveladviser.model.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {
    @GET("place/nearbysearch/json")
    Call<PlacesResponse> getNearbyPlaces(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String apiKey
    );
}