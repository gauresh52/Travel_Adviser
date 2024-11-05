package com.example.traveladviser.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Place {

    @SerializedName("name")
    private String name;

    @SerializedName("formatted_address")
    private String formattedAddress;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("photos")
    private List<Photo> photos;  // For storing photo references

    @SerializedName("rating")
    private float rating;  // For storing rating

    @SerializedName("user_ratings_total")
    private int userRatingsTotal;  // Number of user ratings

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(int userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public static class Photo {
        @SerializedName("photo_reference")
        private String photoReference;

        public String getPhotoReference() {
            return photoReference;
        }

        public void setPhotoReference(String photoReference) {
            this.photoReference = photoReference;
        }
    }
}
