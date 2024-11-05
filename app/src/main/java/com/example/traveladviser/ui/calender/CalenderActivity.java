package com.example.traveladviser.ui.calender;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.traveladviser.R;
import com.example.traveladviser.ui.trips.TripSelectionActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.Date;

public class CalenderActivity extends AppCompatActivity {

    private String destination;
    private TextView selectedDateRangeText; // To display selected date range
    private Date startDate; // Store the selected start date
    private Date endDate;   // Store the selected end date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        // Get destination from intent
        destination = getIntent().getStringExtra("destination");

        // Set destination in TextView
        TextView destinationText = findViewById(R.id.destination_text);
        destinationText.setText(destination + " trip");

        // Initialize TextView for selected date range
        selectedDateRangeText = findViewById(R.id.selected_date_range);

        // Date picker setup
        Button datePickerButton = findViewById(R.id.show_date_picker);
        datePickerButton.setOnClickListener(v -> showCustomDatePicker());

        // Back button action
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Next button action
        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> openTripSelectionActivity());
    }

    // Method to show the custom date picker
    private void showCustomDatePicker() {
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog();

        // Handle the selected date range from the custom date picker
        // Handle the selected date range from the custom date picker
        datePickerDialog.setOnDateSelectedListener((start, end) -> {
            // Store the selected start and end dates
            startDate = new Date(start); // Convert long to Date
            endDate = new Date(end); // If end is also long, convert it similarly
            // Format the selected start and end dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String formattedStartDate = dateFormat.format(startDate);
            String formattedEndDate = dateFormat.format(endDate);

            // Update the TextView with the selected date range
            selectedDateRangeText.setText("Selected Date Range: " + formattedStartDate + " - " + formattedEndDate);
        });

        // Show the custom date picker dialog
        datePickerDialog.show(getSupportFragmentManager(), "CUSTOM_DATE_PICKER");
    }


    private void openTripSelectionActivity() {
        if (startDate != null && endDate != null) {
            // Calculate the number of days between startDate and endDate
            long diffInMillis = endDate.getTime() - startDate.getTime();
            long numberOfDays = TimeUnit.MILLISECONDS.toDays(diffInMillis) + 1; // Include the end date in the count

            // Format the selected date range for passing to the next activity
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String formattedStartDate = dateFormat.format(startDate);
            String formattedEndDate = dateFormat.format(endDate);
            String dateRange = formattedStartDate + " - " + formattedEndDate;

            // Start TripSelectionActivity with the destination, date range, and number of days
            Intent intent = new Intent(CalenderActivity.this, TripSelectionActivity.class);
            intent.putExtra("destination", destination);  // Pass the destination
            intent.putExtra("date_range", dateRange);     // Pass the selected date range
            intent.putExtra("number_of_days", numberOfDays); // Pass the calculated number of days
            startActivity(intent);
        } else {
            Log.e("CalenderActivity", "No dates selected.");
        }
    }

}
