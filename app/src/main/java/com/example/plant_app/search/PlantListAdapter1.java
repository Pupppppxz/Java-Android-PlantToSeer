package com.example.plant_app.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.plant_app.R;
import com.example.plant_app.firebase.PlantListView;

import java.util.ArrayList;

public class PlantListAdapter1 extends ArrayAdapter<PlantListView> {
    private static final String TAG = "PlantListAdapter1";

    private Context mContext;
    int mResource;

    public PlantListAdapter1(Context context, int resource, ArrayList<PlantListView> objects) {
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
        String treatment = getItem(position).getTreatments();
        Bitmap img = getItem(position).getBitmap();
        int index = getItem(position).getIndex();

        PlantListView plantListView = new PlantListView(name, sciName, type, img, index, treatment);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        ImageView pImageView = convertView.findViewById(R.id.ontology_filtered_image);
        TextView pNameView = convertView.findViewById(R.id.ontology_filtered_name);
        TextView pSciNameView = convertView.findViewById(R.id.ontology_filtered_sci_name);

        pNameView.setText(name);
        pSciNameView.setText(sciName);
        pImageView.setImageBitmap(img);

        return convertView;
    }

}
