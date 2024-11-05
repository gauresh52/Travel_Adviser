package com.example.traveladviser.ui.calender;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.traveladviser.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomDatePickerDialog extends DialogFragment {

    private OnDateSelectedListener dateSelectedListener;
    private long startDate;
    private long endDate;

    public interface OnDateSelectedListener {
        void onDateRangeSelected(long startDate, long endDate);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.dateSelectedListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_date_picker);

        CalendarView startCalendarView = dialog.findViewById(R.id.start_calendar_view);
        CalendarView endCalendarView = dialog.findViewById(R.id.end_calendar_view);
        TextView selectedDateRangeText = dialog.findViewById(R.id.selected_date_range_text);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);

        // Set listener for the start calendar view
        startCalendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Create a Calendar object to get the selected start date
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            startDate = calendar.getTimeInMillis();  // Get the selected start date in milliseconds
            updateSelectedDateRangeText(selectedDateRangeText);
        });

        // Set listener for the end calendar view
        endCalendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Create a Calendar object to get the selected end date
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            endDate = calendar.getTimeInMillis();  // Get the selected end date in milliseconds
            updateSelectedDateRangeText(selectedDateRangeText);
        });

        // Confirm button action
        confirmButton.setOnClickListener(v -> {
            if (startDate > endDate) {
                selectedDateRangeText.setText("Invalid range. End date should be after start date.");
            } else if (dateSelectedListener != null) {
                dateSelectedListener.onDateRangeSelected(startDate, endDate);
                dismiss();
            }
        });

        // Cancel button action
        cancelButton.setOnClickListener(v -> dismiss());

        return dialog;
    }

    // Update the TextView with the selected date range
    private void updateSelectedDateRangeText(TextView selectedDateRangeText) {
        if (startDate != 0 && endDate != 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String formattedStartDate = dateFormat.format(startDate);
            String formattedEndDate = dateFormat.format(endDate);
            selectedDateRangeText.setText("Selected Date Range: " + formattedStartDate + " - " + formattedEndDate);
        }
    }
}
