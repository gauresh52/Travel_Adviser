package com.example.traveladviser.ui.recommendations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladviser.R;
import com.example.traveladviser.model.Place;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private List<Place> places;
    private Context context;

    public PlacesAdapter(List<Place> places, Context context) {
        this.places = places;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeName.setText(place.getName());
        holder.placeRating.setText(String.format("Rating: %.1f (%d)", place.getRating(), place.getUserRatingsTotal()));
        holder.placeAddress.setText(place.getFormattedAddress());

        // Load the image using Picasso and Google Places photo reference
        if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
            String photoReference = place.getPhotos().get(0).getPhotoReference();
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + photoReference + "&key=" + "AIzaSyCT3-lL68glxCydrd3zN33_kdwL71hBG6U";
            Picasso.get().load(photoUrl).into(holder.placeImage);
        }

        // Checkbox functionality
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle checkbox logic here
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView placeName, placeAddress, placeRating;
        ImageView placeImage;
        CheckBox checkBox;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.place_name);
            placeAddress = itemView.findViewById(R.id.place_address);
            placeRating = itemView.findViewById(R.id.place_rating);
            placeImage = itemView.findViewById(R.id.place_image);
            checkBox = itemView.findViewById(R.id.place_checkbox);
        }
    }
}
