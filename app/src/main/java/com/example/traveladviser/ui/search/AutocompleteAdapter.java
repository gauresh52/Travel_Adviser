package com.example.traveladviser.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladviser.R;
import java.util.List;
import androidx.appcompat.widget.SearchView;

public class AutocompleteAdapter extends RecyclerView.Adapter<AutocompleteAdapter.ViewHolder> {
    private List<String> predictions;
    private OnItemClickListener onItemClickListener;
    private SearchView searchView;

    public AutocompleteAdapter(List<String> predictions, OnItemClickListener listener, SearchView searchView) {
        this.predictions = predictions;
        this.onItemClickListener = listener;
        this.searchView = searchView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String prediction = predictions.get(position);
        holder.textView.setText(prediction);
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick(prediction);
            searchView.setQuery(prediction, false);  // Set the clicked prediction in SearchView
            searchView.clearFocus();  // Remove focus from SearchView
        });
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public void updatePredictions(List<String> predictions) {
        this.predictions.clear();
        this.predictions.addAll(predictions);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String prediction);
    }
}


