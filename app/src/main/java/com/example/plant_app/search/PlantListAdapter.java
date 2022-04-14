package com.example.plant_app.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.plant_app.HomeActivity;
import com.example.plant_app.R;
import com.example.plant_app.SearchFragment;
import com.example.plant_app.firebase.PlantListView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        String treatment = getItem(position).getTreatments();
        Bitmap img = getItem(position).getBitmap();
        int index = getItem(position).getIndex();

        PlantListView plantListView = new PlantListView(name, sciName, type, img, index, treatment);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        ImageView pImageView = convertView.findViewById(R.id.plant_list_view_image);
        TextView pNameView = convertView.findViewById(R.id.plant_list_view_1);
        TextView pSciNameView = convertView.findViewById(R.id.plant_list_view_2);

        pNameView.setText(name);
        pSciNameView.setText(sciName);
        pImageView.setImageBitmap(img);

        return convertView;
    }
}
