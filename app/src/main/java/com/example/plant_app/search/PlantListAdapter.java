package com.example.plant_app.search;

import android.content.Context;
import android.content.Intent;
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
    Button btnDelete, btnEdit;

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
        int img = getItem(position).getImg();
        int index = getItem(position).getIndex();

        PlantListView plantListView = new PlantListView(name, sciName, type, img, index, treatment);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        try {
            btnEdit = convertView.findViewById(R.id.search_btn_admin_edit);
            btnDelete = convertView.findViewById(R.id.search_btn_admin_delete);

            btnDelete.setOnClickListener(view -> {
                System.out.println("deleteeeeeeeeeeeeeeee" + name);
                deletePlant(type, name);
                Intent intent = new Intent(view.getContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            });

            btnEdit.setOnClickListener(view -> {
                System.out.println("editttttttttttttttttttttttttttt");
            });
        } catch (Exception e) {
            System.out.println(e);
        }

        ImageView pImageView = convertView.findViewById(R.id.plant_list_view_image);
        TextView pNameView = convertView.findViewById(R.id.plant_list_view_1);
        TextView pSciNameView = convertView.findViewById(R.id.plant_list_view_2);

        pNameView.setText(name);
        pSciNameView.setText(sciName);
        pImageView.setImageResource(img);

        return convertView;
    }

    private void deletePlant(String type, String pName) {
        DocumentReference db = FirebaseFirestore.getInstance().collection(type).document(pName);
        db.delete()
                .addOnSuccessListener(unused -> {
                    System.out.println("Deleted!");
                })
                .addOnFailureListener(e -> System.out.println("failed"));
        String userId = FirebaseAuth.getInstance().getUid();
        DocumentReference db1 = FirebaseFirestore.getInstance().collection("LIKE$$" + userId).document(pName);
        db1.delete()
                .addOnSuccessListener(unused -> System.out.println("Deleted!"))
                .addOnFailureListener(e -> System.out.println("failed"));
    }
}
