package com.example.plant_app.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.plant_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProgramAdapter extends ArrayAdapter<String> {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private String userId;

    Context context;
    int[] images;
    String[] plantName;
    String[] plantSciName;
    String[] plantType;

    public ProgramAdapter(Context context, String[] plantName, String[] plantSciName, String[] plantType) {
        super(context, R.layout.list_view_map_item, R.id.plant_list_view_1, plantName);
        initElement();
        this.context = context;
        this.plantName = plantName;
        this.plantSciName = plantSciName;
        this.plantType = plantType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View singleItem = convertView;
        ProgramViewHolder holder = null;
        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.list_view_map_item, parent, false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        } else {
            holder = (ProgramViewHolder) singleItem.getTag();
        }
//        Picasso.get()
//            .load("gs://plant-to-seer.appspot.com/image_uploads/"+ userId + "/" + plant.getType() + "/" + plant.getName())
//            .into(Image))
//        holder.imageView.setImageResource(Picasso.get().load().fit().centerCrop());
//        holder.imageView.setImageResource(Glide.with());
        holder.plantView.setText(plantName[position]);
        holder.plantSciName.setText(plantSciName[position]);
        singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "You clickde: " + plantName[position], Toast.LENGTH_SHORT).show();
            }
        });
        return singleItem;
    }

    private void initElement() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();
    }
}
