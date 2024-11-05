package com.example.traveladviser.ui.trips;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TripSelectionViewModel extends ViewModel {
    private MutableLiveData<String> cuisineType = new MutableLiveData<>();

    public LiveData<String> getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisine) {
        cuisineType.setValue(cuisine);
    }
}