package com.example.traveladviser.ui.search;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivityModel extends ViewModel {
    // Add logic to handle API data, store predictions, etc.

    // For example, you might store search results:
    private List<String> autocompleteSuggestions = new ArrayList<>();

    public List<String> getAutocompleteSuggestions() {
        return autocompleteSuggestions;
    }

    public void setAutocompleteSuggestions(List<String> suggestions) {
        autocompleteSuggestions = suggestions;
    }

    // Additional logic for handling autocomplete predictions, errors, etc.
}
