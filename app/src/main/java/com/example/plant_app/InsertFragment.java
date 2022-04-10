package com.example.plant_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.insert.InsertFruitFragment;
import com.example.plant_app.insert.InsertHerbFragment;
import com.example.plant_app.insert.InsertVegetableFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URL;

public class InsertFragment extends Fragment {

    private ImageButton buttonInsertVegetable;
    private ImageButton buttonInsertFruit;
    private ImageButton buttonInsertHerb;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImageView profilePicture;

    public InsertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_insert, container, false);

        buttonInsertVegetable = v.findViewById(R.id.insertImageVeg);
        buttonInsertFruit = v.findViewById(R.id.insertImageFruit);
        buttonInsertHerb = v.findViewById(R.id.insertImageHerb);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        String userId = firebaseUser.getUid();
        if (firebaseUser == null) {
            navigateToMain();
        }

        storageReference = storage.getReference()
                .child(FirebaseLocal.storagePathForImageUpload + userId + "/profile");
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("downloaded image");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profilePicture = v.findViewById(R.id.insert_profile_picture);
                            profilePicture.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(e -> System.out.println(e));
        } catch (Exception e) {
            System.out.println(e);
        }

        buttonInsertVegetable.setOnClickListener(view -> replaceFragment(new InsertVegetableFragment()));

        buttonInsertFruit.setOnClickListener(view -> replaceFragment(new InsertFruitFragment()));

        buttonInsertHerb.setOnClickListener(view -> replaceFragment(new InsertHerbFragment()));

        return v;
    }

    private void navigateToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrameLayout, fragment);
        fragmentTransaction.commit();
    }
}