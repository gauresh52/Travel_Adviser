package com.example.traveladviser.ui.search;

import android.content.Intent; // Add this import
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button; // Add this import
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladviser.R;
import com.example.traveladviser.ui.calender.CalenderActivity; // Add this import
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private PlacesClient placesClient;
    private AutocompleteAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView; // Declare SearchView at class level
    private String lastQuery = ""; // Variable to store the last query

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize Places API
        Places.initialize(getApplicationContext(), "AIzaSyCT3-lL68glxCydrd3zN33_kdwL71hBG6U");
        placesClient = Places.createClient(this);

        // Initialize SearchView
        searchView = findViewById(R.id.search_view); // Initialize SearchView here
        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AutocompleteAdapter(new ArrayList<>(), prediction -> {
            // Handle item click and update SearchView
            Log.i("Autocomplete", "Selected: " + prediction);
            searchView.setQuery(prediction, false); // Set selected prediction in SearchView
            recyclerView.setVisibility(View.GONE); // Hide suggestions
            lastQuery = prediction; // Update lastQuery
        }, searchView); // Pass searchView as the third argument
        recyclerView.setAdapter(adapter);

        // Set up the next button
        Button nextButton = findViewById(R.id.next_button); // Initialize Next Button
        nextButton.setOnClickListener(v -> openCalenderActivity()); // Set click listener

        setupSearchAutocomplete();
    }

    private void setupSearchAutocomplete() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty() && !newText.equals(lastQuery)) { // Check against lastQuery
                    // Token for autocomplete session
                    AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

                    // Create autocomplete request
                    FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(token)
                            .setQuery(newText)
                            .build();

                    // Execute the request
                    placesClient.findAutocompletePredictions(request)
                            .addOnSuccessListener(response -> {
                                List<String> predictions = new ArrayList<>();
                                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                    predictions.add(prediction.getPrimaryText(null).toString());
                                }
                                adapter.updatePredictions(predictions); // Use a method to update predictions
                                recyclerView.setVisibility(View.VISIBLE);  // Show the results
                            })
                            .addOnFailureListener(e -> Log.e("Autocomplete", "Error: " + e.getMessage()));
                } else {
                    recyclerView.setVisibility(View.GONE);  // Hide the list when no input
                }
                return false;
            }
        });
    }

    private void openCalenderActivity() {
        Intent intent = new Intent(SearchActivity.this, CalenderActivity.class);
        // Pass the selected destination to CalenderActivity if needed
        String selectedDestination = searchView.getQuery().toString(); // Get the current query from SearchView
        intent.putExtra("destination", selectedDestination);
        startActivity(intent); // Start CalenderActivity
    }
}
