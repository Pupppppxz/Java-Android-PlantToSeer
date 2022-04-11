package com.example.plant_app.edit_plant;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.plant_app.R;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.insert.InitSpinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditPlantFragment extends Fragment {

    private static final String TAG = "EditPlantFragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private String userId;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private ImageView imageSelect;
    
    private Plant plant;
    Spinner botanical;
    Spinner fruit;
    private EditText name, scienceName, families, familyDescription, description, season, vitamin, mineral, harvestTime, treatments,
            planting, soil, soilPH, sunExposure, water, temperature, humidity, fertilizer;
    private TextView classi;
    private Button btnSubmit, btnReset;
    private ProgressDialog progressDialog;
    String[] items1 = new String[]{
            "-- Botanical habit type --", "Herbaceous stem", "Perennial plant", "Shrub", "Climber", "Scandent"
    };
    String[] items2 = new String[]{
            "-- Fruit type --", "Multiple Fruit", "Simple Fruit", "Aggregate Fruit"
    };
    String[] items3 = new String[]{
            "-- Vegetable type --", "Bulb", "Leaf", "Pod", "Flower"
    };

    public EditPlantFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_plant, container, false);
        
        initElement(v);

        return v;
    }

    private void initElement(View v) {
        name = v.findViewById(R.id.edit_fruit_name);
        scienceName = v.findViewById(R.id.edit_fruit_sci_name);
        families = v.findViewById(R.id.edit_fruit_family);
        familyDescription = v.findViewById(R.id.edit_fruit_family_desc);
        description = v.findViewById(R.id.edit_fruit_desc);
        season = v.findViewById(R.id.edit_fruit_season);
        vitamin = v.findViewById(R.id.edit_fruit_vitamin);
        mineral = v.findViewById(R.id.edit_fruit_mineral);
        harvestTime = v.findViewById(R.id.edit_fruit_harv);
        treatments = v.findViewById(R.id.edit_fruit_treatment);
        planting = v.findViewById(R.id.edit_fruit_planting);
        soil = v.findViewById(R.id.edit_fruit_soil);
        soilPH = v.findViewById(R.id.edit_fruit_soilpH);
        sunExposure = v.findViewById(R.id.edit_fruit_sun);
        water = v.findViewById(R.id.edit_fruit_water);
        temperature = v.findViewById(R.id.edit_fruit_temp);
        humidity = v.findViewById(R.id.edit_fruit_humi);
        fertilizer = v.findViewById(R.id.edit_fruit_fert);
        btnSubmit = v.findViewById(R.id.editFruitBtnSubmit);
        btnReset = v.findViewById(R.id.editFruitBtnReset);
        imageSelect = v.findViewById(R.id.btnImageEdit);
        botanical = v.findViewById(R.id.edit_fruit_bot_habit);
        fruit = v.findViewById(R.id.edit_fruit_bot_classi);
        progressDialog = new ProgressDialog(getActivity());

        classi = v.findViewById(R.id.edit_classificaton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        botanical.setDropDownVerticalOffset(100);
        InitSpinner.setInitSpinner(items1, botanical, getActivity());

        if (plant.getType().equalsIgnoreCase("HERB")) {
            classi.setVisibility(v.GONE);
            fruit.setVisibility(v.GONE);
        } else if (plant.getType().equalsIgnoreCase("FRUIT")) {
            fruit.setDropDownVerticalOffset(100);
            InitSpinner.setInitSpinner(items2, fruit, getActivity());
        } else {
            classi.setText("Classification of Vegetable:");
            fruit.setDropDownVerticalOffset(100);
            InitSpinner.setInitSpinner(items3, fruit, getActivity());
        }
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}