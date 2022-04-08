package com.example.plant_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.plant_app.insert.InsertFruitFragment;
import com.example.plant_app.insert.InsertHerbFragment;
import com.example.plant_app.insert.InsertVegetableFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InsertFragment extends Fragment {

    private ImageButton buttonInsertVegetable;
    private ImageButton buttonInsertFruit;
    private ImageButton buttonInsertHerb;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

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
        if (firebaseUser == null) {
            navigateToMain();
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