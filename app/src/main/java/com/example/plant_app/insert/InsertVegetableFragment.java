package com.example.plant_app.insert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.plant_app.HomeActivity;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.insert.InitSpinner;

import com.example.plant_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InsertVegetableFragment extends Fragment {

    private static final String TAG = "InsertVegetableFragment";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private String userId;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    ImageButton btnImageInsertVeg;
    Spinner botanical;
    Spinner vegetable;
    private EditText name, scienceName, families, familyDescription, description, season, vitamin, mineral, harvestTime, treatments,
            planting, soil, soilPH, sunExposure, water, temperature, humidity, fertilizer;
    private Button btnSubmit, btnReset;
    private ProgressDialog progressDialog;
    String[] items1 = new String[]{
            "-- Botanical habit type --", "Herbaceous stem", "Perennial plant", "Shrub", "Climber", "Scandent"
    };
    String[] items2 = new String[]{
            "-- Vegetable type --", "Bulb", "Leaf", "Pod", "Flower"
    };

    public InsertVegetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_insert_vegetable, container, false);

        initElement(v);

        btnImageInsertVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForm();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("click btn submit");
                PerformInsertVegetable();
            }
        });

        return v;
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            btnImageInsertVeg.setImageBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void PerformInsertVegetable() {
        String inputName = name.getText().toString();
        String inputScienceName = scienceName.getText().toString();
        String inputFamilies = families.getText().toString();
        String inputFamilyDescription = familyDescription.getText().toString();
        String inputDescription = description.getText().toString();
        String inputSeason = season.getText().toString();
        String inputVitamin = vitamin.getText().toString();
        String inputMineral = mineral.getText().toString();
        String inputHarvestTime = harvestTime.getText().toString();
        String inputTreatments = treatments.getText().toString();
        String inputPlanting = planting.getText().toString();
        String inputSoil = soil.getText().toString();
        String inputSoilPH = soilPH.getText().toString();
        String inputSunExposure = sunExposure.getText().toString();
        String inputWater = water.getText().toString();
        String inputTemperature = temperature.getText().toString();
        String inputHumidity = humidity.getText().toString();
        String inputFertilizer = fertilizer.getText().toString();
        String spinnerBotanical = botanical.getSelectedItem().toString();
        String spinnerVegetable = vegetable.getSelectedItem().toString();

        if (inputName.isEmpty()) {
            name.setError("Enter name field");
        } else if (inputScienceName.isEmpty()) {
            scienceName.setError("Enter science name");
        } else if (inputFamilies.isEmpty()) {
            families.setError("Enter families");
        } else if (inputFamilyDescription.isEmpty()) {
            familyDescription.setError("Enter family description");
        } else if (inputDescription.isEmpty()) {
            description.setError("Enter description");
        } else if (inputSeason.isEmpty()) {
            season.setError("Enter season");
        } else if (inputVitamin.isEmpty()) {
            vitamin.setError("Enter vitamin");
        } else if (inputMineral.isEmpty()) {
            mineral.setError("Enter mineral");
        } else if (inputHarvestTime.isEmpty()) {
            harvestTime.setError("Enter harvest time");
        } else if (inputPlanting.isEmpty()) {
            planting.setError("Enter planting");
        } else if (inputSoil.isEmpty()) {
            soil.setError("Enter soil");
        } else if (inputSoilPH.isEmpty()) {
            soilPH.setError("Enter soil pH");
        } else if (inputSunExposure.isEmpty()) {
            sunExposure.setError("Enter sun exposure");
        } else if (inputWater.isEmpty()) {
            water.setError("Enter water");
        } else if (inputTemperature.isEmpty()) {
            temperature.setError("Enter temperature");
        } else if (inputHumidity.isEmpty()) {
            humidity.setError("Enter humidity");
        } else if (inputFertilizer.isEmpty()) {
            fertilizer.setError("Enter fertilizer");
        } else if (spinnerBotanical == "-- Botanical habit type --") {
            Toast
                    .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Select botanical habit</b></font>"), Toast.LENGTH_SHORT)
                    .show();
        } else if (spinnerVegetable == "-- Vegetable type --"){
            Toast
                    .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Select vegetable type</b></font>"), Toast.LENGTH_SHORT)
                    .show();
        } else {

            db.collection("VEGETABLE").document(inputName).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (!doc.exists()) {
                                if (filePath != null) {
                                    progressDialog.setMessage("Please Wait While Insert vegetable..");
                                    progressDialog.setTitle("Insert Vegetable");
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();

                                    StorageReference ref = storageReference
                                            .child(FirebaseLocal.storagePathForImageUpload + userId + "/vegetables/" + inputName);

                                    ref.putFile(filePath)
                                            .addOnSuccessListener(taskSnapshot -> {
                                                progressDialog.dismiss();
                                                saveNewVegetable(inputName, inputScienceName, inputFamilies, inputFamilyDescription, spinnerBotanical, inputDescription,
                                                        inputSeason, inputVitamin, inputMineral, inputHarvestTime, inputTreatments, spinnerVegetable, inputPlanting,
                                                        inputSoil, inputSoilPH, inputSunExposure, inputWater, inputTemperature, inputHumidity, inputFertilizer);
                                                Toast
                                                        .makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT)
                                                        .show();
                                            })
                                            .addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                Toast
                                                        .makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT)
                                                        .show();
                                            })
                                            .addOnProgressListener(taskSnapshot -> {
                                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                            })
                                            .addOnCompleteListener(tasks -> sendUserToHome());
                                } else {
                                    Toast
                                            .makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT)
                                            .show();
                                }
                                System.out.println("not exist");
                            } else {
                                Toast
                                        .makeText(getActivity(), "Plant name already exist", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast
                                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveNewVegetable(String sname, String ssciName, String sfamily, String sfamilyDesc, String sbotanical, String sdescription, String sseason,
                                  String svitamin, String smineral, String sharvest_time, String streatments, String svegetable_type, String splanting,
                                  String ssoil, String ssoilPH, String ssunExposure, String swater, String stemp, String shumidity, String sfertilizer) {
        String t = "VEGETABLE";
        if (!streatments.equals("")) {
            t += ",HERB";
        }
        Map<String, Object> vegetable = new HashMap<>();
        vegetable.put(KeyInsert.KEY_NAME, sname);
        vegetable.put(KeyInsert.KEY_SCIENCE_NAME, ssciName);
        vegetable.put(KeyInsert.KEY_FAMILIES, sfamily);
        vegetable.put(KeyInsert.KEY_FAMILY_DESC, sfamilyDesc);
        vegetable.put(KeyInsert.KEY_BOTANICAL_HABIT, sbotanical);
        vegetable.put(KeyInsert.KEY_DESCRIPTION, sdescription);
        vegetable.put(KeyInsert.KEY_SEASON, sseason);
        vegetable.put(KeyInsert.KEY_VITAMIN, svitamin);
        vegetable.put(KeyInsert.KEY_MINERAL, smineral);
        vegetable.put(KeyInsert.KEY_HARVEST_TIME, sharvest_time);
        vegetable.put(KeyInsert.KEY_TREATMENTS, streatments);
        vegetable.put(KeyInsert.KEY_TYPE, svegetable_type);
        vegetable.put(KeyInsert.KEY_PLANTING, splanting);
        vegetable.put(KeyInsert.KEY_SOIL, ssoil);
        vegetable.put(KeyInsert.KEY_SOIL_PH, ssoilPH);
        vegetable.put(KeyInsert.KEY_SUN_EXPOSURE, ssunExposure);
        vegetable.put(KeyInsert.KEY_WATER, swater);
        vegetable.put(KeyInsert.KEY_TEMPERATURE, stemp);
        vegetable.put(KeyInsert.KEY_HUMIDITY, shumidity);
        vegetable.put(KeyInsert.KEY_FERTILIZER, sfertilizer);
        vegetable.put(KeyInsert.TYPE, t);
        vegetable.put(KeyInsert.OWNER, userId);

        db.collection(userId).document(sname).set(vegetable)
                .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Insert vegetable successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                });

        db.collection("VEGETABLE").document(sname).set(vegetable)
                .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Insert vegetable successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                });
    }

    private void clearForm() {
        name.setText("");
        scienceName.setText("");
        families.setText("");
        familyDescription.setText("");
        description.setText("");
        season.setText("");
        vitamin.setText("");
        mineral.setText("");
        harvestTime.setText("");
        treatments.setText("");
        planting.setText("");
        soil.setText("");
        soilPH.setText("");
        sunExposure.setText("");
        water.setText("");
        temperature.setText("");
        humidity.setText("");
        fertilizer.setText("");
        botanical.setSelection(0);
        vegetable.setSelection(0);
    }

    private void sendUserToHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initElement(View v) {
        name = v.findViewById(R.id.in_veg_name);
        scienceName = v.findViewById(R.id.in_veg_sci_name);
        families = v.findViewById(R.id.in_veg_family);
        familyDescription = v.findViewById(R.id.in_veg_family_desc);
        description = v.findViewById(R.id.in_veg_desc);
        season = v.findViewById(R.id.in_veg_season);
        vitamin = v.findViewById(R.id.in_veg_vitamin);
        mineral = v.findViewById(R.id.in_veg_mineral);
        harvestTime = v.findViewById(R.id.in_veg_harv);
        treatments = v.findViewById(R.id.in_veg_treatment);
        planting = v.findViewById(R.id.in_veg_planting);
        soil = v.findViewById(R.id.in_veg_soil);
        soilPH = v.findViewById(R.id.in_veg_soilpH);
        sunExposure = v.findViewById(R.id.in_veg_sun);
        water = v.findViewById(R.id.in_veg_water);
        temperature = v.findViewById(R.id.in_veg_temp);
        humidity = v.findViewById(R.id.in_veg_humi);
        fertilizer = v.findViewById(R.id.in_veg_fert);
        vegetable = v.findViewById(R.id.in_veg_bot_classi);
        botanical = v.findViewById(R.id.in_veg_bot_habit);
        btnImageInsertVeg = v.findViewById(R.id.btnImageInsertVeg);
        btnSubmit = v.findViewById(R.id.in_btn_submit);
        btnReset = v.findViewById(R.id.in_btn_reset);
        progressDialog = new ProgressDialog(getActivity());

        botanical.setDropDownVerticalOffset(100);
        InitSpinner.setInitSpinner(items1, botanical, getActivity());

        vegetable.setDropDownVerticalOffset(100);
        InitSpinner.setInitSpinner(items2, vegetable, getActivity());
        
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();
    }

}