package com.example.traveladviser.ui.trips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import com.example.traveladviser.R;
import com.example.traveladviser.ui.calender.CalenderActivity;
import com.example.traveladviser.ui.recommendations.RecommendationsActivity;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;

public class TripSelectionActivity extends AppCompatActivity {
    private PlacesClient placesClient;
    private String cuisineType = "";

    private String selectedTripType = "";

    // Define the checkboxes
    private CheckBox mustSeeAttractionsCheckBox;
    private CheckBox greatFoodCheckBox;
    private CheckBox hiddenGemsCheckBox;
    private CheckBox cuisineCheckBox;
    private CheckBox waterSportsCheckBox;
    private CheckBox wildlifeSanctuariesCheckBox;
    private CheckBox heritageArchitectureCheckBox;
    private CheckBox nightMarketsCheckBox;
    private CheckBox beachShacksCheckBox;

    // Define class-level variables
    private String selectedDestination;
    private String dateRange;
    private long numberOfDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_selection);

        // Retrieve the intent extras
        Intent intent = getIntent();
        selectedDestination = intent.getStringExtra("destination");
        dateRange = intent.getStringExtra("date_range");
        numberOfDays = intent.getLongExtra("number_of_days", 0);

        // Initialize UI components
        mustSeeAttractionsCheckBox = findViewById(R.id.must_see_attractions);
        greatFoodCheckBox = findViewById(R.id.great_food);
        hiddenGemsCheckBox = findViewById(R.id.hidden_gems);
        cuisineCheckBox = findViewById(R.id.cuisine_checkbox);
        waterSportsCheckBox = findViewById(R.id.water_sports);
        wildlifeSanctuariesCheckBox = findViewById(R.id.wildlife_sanctuaries);
        heritageArchitectureCheckBox = findViewById(R.id.heritage_architecture);
        nightMarketsCheckBox = findViewById(R.id.night_markets);
        beachShacksCheckBox = findViewById(R.id.beach_shacks);

        // Fetch cuisine based on the selected destination
        if (selectedDestination != null) {
            setCusineCheckBox(selectedDestination);
        }

        Button soloTripButton = findViewById(R.id.solo_trip_button);
        Button partnerTripButton = findViewById(R.id.partner_trip_button);
        Button friendsTripButton = findViewById(R.id.friends_trip_button);
        Button familyTripButton = findViewById(R.id.family_trip_button);

        soloTripButton.setOnClickListener(v -> {
            selectedTripType = "Solo Trip";
            updateTripSelectionUI(soloTripButton); // Update UI based on selection
        });

        partnerTripButton.setOnClickListener(v -> {
            selectedTripType = "Partner Trip";
            updateTripSelectionUI(partnerTripButton);
        });

        friendsTripButton.setOnClickListener(v -> {
            selectedTripType = "Friends Trip";
            updateTripSelectionUI(friendsTripButton);
        });

        familyTripButton.setOnClickListener(v -> {
            selectedTripType = "Family Trip";
            updateTripSelectionUI(familyTripButton);
        });

        // Handle the Submit button click
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> openRecommendationsActivity());
    }

    // Method to fetch cuisine using Google Places API based on location
    private void setCusineCheckBox(String location) {

        cuisineType = "Cuisine of "+location;
        cuisineCheckBox.setText(cuisineType);


    }

    private void openRecommendationsActivity() {
        Intent intent = new Intent(TripSelectionActivity.this, RecommendationsActivity.class);

        // Pass the class-level variables to the intent
        intent.putExtra("destination", selectedDestination);
        intent.putExtra("date_range", dateRange);
        intent.putExtra("number_of_days", numberOfDays);
        intent.putExtra("trip_type", selectedTripType);

        // Pass the checked status of checkboxes
        intent.putExtra("must_see_attractions", mustSeeAttractionsCheckBox.isChecked());
        intent.putExtra("great_food", greatFoodCheckBox.isChecked());
        intent.putExtra("hidden_gems", hiddenGemsCheckBox.isChecked());
        intent.putExtra("cuisine", cuisineCheckBox.isChecked());
        intent.putExtra("water_sports", waterSportsCheckBox.isChecked());
        intent.putExtra("wildlife_sanctuaries", wildlifeSanctuariesCheckBox.isChecked());
        intent.putExtra("heritage_architecture", heritageArchitectureCheckBox.isChecked());
        intent.putExtra("night_markets", nightMarketsCheckBox.isChecked());
        intent.putExtra("beach_shacks", beachShacksCheckBox.isChecked());

        // Start the RecommendationsActivity
        startActivity(intent);
    }


    private void updateTripSelectionUI(Button selectedButton) {
        // Reset all buttons first
        resetTripSelectionUI();

        // Highlight the selected button (for example, change background color)
        selectedButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
    }

    private void resetTripSelectionUI() {
        // Reset the appearance of all trip buttons
        findViewById(R.id.solo_trip_button).setBackgroundColor(getResources().getColor(R.color.primaryColor));
        findViewById(R.id.partner_trip_button).setBackgroundColor(getResources().getColor(R.color.primaryColor));
        findViewById(R.id.friends_trip_button).setBackgroundColor(getResources().getColor(R.color.primaryColor));
        findViewById(R.id.family_trip_button).setBackgroundColor(getResources().getColor(R.color.primaryColor));
    }
}
