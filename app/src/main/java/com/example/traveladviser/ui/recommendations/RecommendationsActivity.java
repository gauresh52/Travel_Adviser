package com.example.traveladviser.ui.recommendations;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladviser.R;
import com.example.traveladviser.model.GeocodingResponse;
import com.example.traveladviser.model.Place;
import com.example.traveladviser.model.PlacesResponse;
import com.example.traveladviser.network.GeocodingApi;
import com.example.traveladviser.network.PlacesApi;
import com.example.traveladviser.network.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LinearLayout interestsContainer;

    private static final String API_KEY = "AIzaSyCT3-lL68glxCydrd3zN33_kdwL71hBG6U";

    private String selectedDestination;
    private String tripType;
    private List<String> interests;
    private Map<String, String> interestTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        // Retrieve data from the intent
        selectedDestination = getIntent().getStringExtra("destination");
        tripType = getIntent().getStringExtra("trip_type");
        interests = new ArrayList<>();

        // Retrieve checkbox values
        if (getIntent().getBooleanExtra("must_see_attractions", false)) {
            interests.add("Must See Attractions");
        }
        if (getIntent().getBooleanExtra("great_food", false)) {
            interests.add("Great Food");
        }
        if (getIntent().getBooleanExtra("hidden_gems", false)) {
            interests.add("Hidden Gems");
        }
        if (getIntent().getBooleanExtra("cuisine", false)) {
            interests.add("Cuisine");
        }
        if (getIntent().getBooleanExtra("water_sports", false)) {
            interests.add("Water Sports");
        }
        if (getIntent().getBooleanExtra("wildlife_sanctuaries", false)) {
            interests.add("Wildlife Sanctuaries");
        }
        if (getIntent().getBooleanExtra("heritage_architecture", false)) {
            interests.add("Heritage Architecture");
        }
        if (getIntent().getBooleanExtra("night_markets", false)) {
            interests.add("Night Markets");
        }
        if (getIntent().getBooleanExtra("beach_shacks", false)) {
            interests.add("Beach Shacks");
        }

        // Map interests to corresponding Google Places API types
        interestTypes = new HashMap<>();
        interestTypes.put("Must See Attractions", "tourist_attraction");
        interestTypes.put("Great Food", "restaurant");
        interestTypes.put("Hidden Gems", "hidden_gems");
        interestTypes.put("Cuisine", "cafe");
        interestTypes.put("Water Sports", "water_sports");
        interestTypes.put("Wildlife Sanctuaries", "zoo");
        interestTypes.put("Heritage Architecture", "museum");
        interestTypes.put("Night Markets", "shopping_mall");
        interestTypes.put("Beach Shacks", "beach");

        // Set up the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize the container for interest sections
        interestsContainer = findViewById(R.id.interests_container);

        // Fetch the bounds and radius for the selected destination
        getBoundsForDestination(selectedDestination);
    }

    private void fetchRecommendations(String location, int radius) {
        // For each selected interest, fetch places and add them to the corresponding dropdown section
        for (String interest : interests) {
            String type = interestTypes.get(interest);  // Get the type corresponding to the interest
            if (type != null) {
                createInterestSection(interest);  // Create section for each interest
                loadPlacesForInterest(location, type, interest, radius);  // Fetch places based on interest type and radius
            }
        }
    }

    private void createInterestSection(String interestTitle) {
        // Inflate the custom layout for each interest section
        View sectionView = LayoutInflater.from(this).inflate(R.layout.interest_section, interestsContainer, false);

        // Set the title for the section (interest name)
        TextView interestTitleView = sectionView.findViewById(R.id.interest_title);
        interestTitleView.setText(interestTitle);

        // Add the section to the interestsContainer
        interestsContainer.addView(sectionView);
    }

    private void loadPlacesForInterest(String location, String type, String interestTitle, int radius) {
        PlacesApi apiService = RetrofitClient.getRetrofitInstance().create(PlacesApi.class);

        Call<PlacesResponse> call = apiService.getNearbyPlaces(location, radius, type, API_KEY);

        call.enqueue(new Callback<PlacesResponse>() {
            @Override
            public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                if (response.isSuccessful()) {
                    List<Place> places = response.body().getResults();
                    updateMap(places);  // Add markers to the map
                    updateInterestSection(interestTitle, places);  // Show places in the corresponding section
                } else {
                    Log.e("API_ERROR", "Error in API call");
                }
            }

            @Override
            public void onFailure(Call<PlacesResponse> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }


    private void updateInterestSection(String interestTitle, List<Place> places) {
        // Find the section by interest title
        for (int i = 0; i < interestsContainer.getChildCount(); i++) {
            View sectionView = interestsContainer.getChildAt(i);
            TextView titleView = sectionView.findViewById(R.id.interest_title);
            if (titleView.getText().toString().equals(interestTitle)) {
                RecyclerView recyclerView = sectionView.findViewById(R.id.places_recycler_view);
                TextView noPlacesMessage = sectionView.findViewById(R.id.no_places_message);

                if (places.isEmpty()) {
                    // Show "No places found" message if the list is empty
                    noPlacesMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    // Hide the no places message and show the recycler view
                    noPlacesMessage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    PlacesAdapter adapter = new PlacesAdapter(places, this);
                    recyclerView.setAdapter(adapter);
                }
                break;
            }
        }
    }

    private void updateMap(List<Place> places) {
        for (Place place : places) {
            LatLng location = new LatLng(place.getGeometry().getLocation().getLat(),
                    place.getGeometry().getLocation().getLng());
            mMap.addMarker(new MarkerOptions().position(location).title(place.getName()));
        }
        // Move camera to the first place
        if (!places.isEmpty()) {
            LatLng firstPlace = new LatLng(places.get(0).getGeometry().getLocation().getLat(),
                    places.get(0).getGeometry().getLocation().getLng());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPlace, 12));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    private void getBoundsForDestination(String destination) {
        // Fetch bounds from Geocoding API
        GeocodingApi apiService = RetrofitClient.getRetrofitInstance().create(GeocodingApi.class);

        Call<GeocodingResponse> call = apiService.getCoordinates(destination, API_KEY);
        call.enqueue(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && !response.body().getResults().isEmpty()) {
                    GeocodingResponse.Result.Geometry.Bounds bounds = response.body()
                            .getResults().get(0).getGeometry().getBounds();

                    if (bounds != null) {
                        LatLng northeast = new LatLng(bounds.getNortheast().getLat(), bounds.getNortheast().getLng());
                        LatLng southwest = new LatLng(bounds.getSouthwest().getLat(), bounds.getSouthwest().getLng());

                        // Calculate radius based on the size of the location
                        int calculatedRadius = calculateRadiusFromBounds(northeast, southwest);
                        Log.d("DYNAMIC_RADIUS", "Calculated Radius: " + calculatedRadius);

                        // Fetch recommendations
                        String centerLocation = response.body().getResults().get(0).getGeometry().getCoordinates().getLat() +
                                "," +
                                response.body().getResults().get(0).getGeometry().getCoordinates().getLng();
                        fetchRecommendations(centerLocation, calculatedRadius);
                    } else {
                        Log.e("GEO_ERROR", "Bounds not available for the destination");
                    }
                } else {
                    Log.e("GEO_ERROR", "Error in Geocoding API response");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Log.e("GEO_ERROR", t.getMessage());
            }
        });
    }

    private int calculateRadiusFromBounds(LatLng northeast, LatLng southwest) {
        // Calculate the diagonal distance between the northeast and southwest corners of the bounds
        double distanceInMeters = SphericalUtil.computeDistanceBetween(northeast, southwest);

        // Set the radius to half of this diagonal distance (so it covers the entire location)
        return (int) (distanceInMeters / 2);
    }
}
