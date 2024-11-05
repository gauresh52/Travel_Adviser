package com.example.traveladviser.network;

import com.example.traveladviser.model.GeocodingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingApi {
    @GET("geocode/json")
    Call<GeocodingResponse> getCoordinates(
            @Query("address") String address,
            @Query("key") String apiKey
    );
}