package com.example.plant_app.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plant_app.R;
import com.example.plant_app.firebase.PlantListView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private List<PlantListView> plantListsss;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.image_image_recycle_item);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PlantListView plant = plantListsss.get(position);
        holder.img.setImageBitmap(plant.getBitmap());
    }

    @Override
    public int getItemCount() {
        return plantListsss.size();
    }

    public ImageAdapter(List<PlantListView> plantList) {
        this.plantListsss = new ArrayList<>(plantList);
    }
}
