package com.example.plant_app.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.plant_app.R;
import com.example.plant_app.firebase.PlantListView;

import java.util.ArrayList;

public class PlantListAdapter extends ArrayAdapter<PlantListView> {
    private static final String TAG = "PlantListAdapter";

    private Context mContext;
    int mResource;

    public PlantListAdapter(Context context, int resource, ArrayList<PlantListView> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String sciName = getItem(position).getSciName();
        String type = getItem(position).getType();
        int img = getItem(position).getImg();

        PlantListView plantListView = new PlantListView(name, sciName, type, img);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        ImageView pImageView = convertView.findViewById(R.id.plant_list_view_image);
        TextView pNameView = convertView.findViewById(R.id.plant_list_view_1);
        TextView pSciNameView = convertView.findViewById(R.id.plant_list_view_2);

        pNameView.setText(name);
        pSciNameView.setText(sciName);
        pImageView.setImageResource(img);

        return convertView;
    }
}
