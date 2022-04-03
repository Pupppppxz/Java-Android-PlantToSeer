package com.example.plant_app.search;

import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_app.R;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.insert.InitSpinner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProgramViewHolder {

    ImageView imageView;
    TextView plantView;
    TextView plantSciName;
    ProgramViewHolder(View v) {
//        Picasso.get()
//            .load("gs://plant-to-seer.appspot.com/image_uploads/"+ userId + "/" + plant.getType() + "/" + plant.getName())
//            .into(Image))
//        imageView = v.findViewById(R.id.listViewImage);
//        plantView = v.findViewById(R.id.listViewText1);
//        plantSciName = v.findViewById(R.id.listViewText2);
    }
}
