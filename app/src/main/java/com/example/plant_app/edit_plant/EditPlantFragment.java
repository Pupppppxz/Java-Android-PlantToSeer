package com.example.plant_app.edit_plant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_app.HomeActivity;
import com.example.plant_app.R;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantLiked;
import com.example.plant_app.firebase.User;
import com.example.plant_app.insert.InitSpinner;
import com.example.plant_app.insert.KeyInsert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private String plantName, plantType;
    Spinner botanical;
    Spinner fruit;
    private EditText name, scienceName, families, familyDescription, description, season, vitamin, mineral, harvestTime, treatments,
            planting, soil, soilPH, sunExposure, water, temperature, humidity, fertilizer;
    private TextView classi, plantNameTopic;
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
    private ArrayList<User> allUser = new ArrayList<>();

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

        initText(v);

        btnReset.setOnClickListener(view -> sendUserToActivity());

        btnSubmit.setOnClickListener(view -> updatePlant());

        imageSelect.setOnClickListener(view -> SelectImage());
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
            imageSelect.setImageBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initText(View v) {
        name.setText(plant.getName());
        scienceName.setText(plant.getScienceName());
        families.setText(plant.getFamilies());
        familyDescription.setText(plant.getFamilyDescription());
        description.setText(plant.getDescription());
        season.setText(plant.getSeason());
        vitamin.setText(plant.getVitamin());
        mineral.setText(plant.getMineral());
        harvestTime.setText(plant.getHarvestTime());
        treatments.setText(plant.getTreatments());
        planting.setText(plant.getPlanting());
        soil.setText(plant.getSoil());
        soilPH.setText(plant.getSoilPH());
        sunExposure.setText(plant.getSunExposure());
        water.setText(plant.getWater());
        temperature.setText(plant.getTemperature());
        humidity.setText(plant.getHumidity());
        fertilizer.setText(plant.getFertilizer());
        plantNameTopic.setText(plant.getName());

        for (int i = 0; i < items1.length; i++) {
            if (plant.getBotanicalHabit().equalsIgnoreCase(items1[i])) {
                botanical.setSelection(i);
                break;
            }
        }

        if (plant.getType().split(",")[0].equalsIgnoreCase("FRUIT")) {
            for (int i = 0; i < items2.length; i++) {
                if (plant.getClassification().equalsIgnoreCase(items2[i])) {
                    fruit.setSelection(i);
                    break;
                }
            }
        } else if (plant.getType().split(",")[0].equalsIgnoreCase("VEGETABLE")) {
            for (int i = 0; i < items3.length; i++) {
                if (plant.getClassification().equalsIgnoreCase(items3[i])) {
                    fruit.setSelection(i);
                    break;
                }
            }
        }
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
        plantNameTopic = v.findViewById(R.id.edit_plant_name_topic);

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
        } else if (plant.getType().split(",")[0].equalsIgnoreCase("FRUIT")) {
            fruit.setDropDownVerticalOffset(100);
            InitSpinner.setInitSpinner(items2, fruit, getActivity());
        } else {
            classi.setText("Classification of Vegetable:");
            fruit.setDropDownVerticalOffset(100);
            InitSpinner.setInitSpinner(items3, fruit, getActivity());
        }
    }

    private void getAllUsers() {
        System.out.println("start get all user");
        db.collection("User").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot users : queryDocumentSnapshots) {
                            User usersss = users.toObject(User.class);
                            System.out.println("get user = " + usersss);
                            allUser.add(usersss);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show())
                .addOnCompleteListener(task -> {
                    System.out.println("end get all user");
                    checkUserLiked();
                });
    }

    public void checkUserLiked() {
        String eName = name.getText().toString();
        for (int i = 0; i < allUser.size(); i++) {
            String id = allUser.get(i).getUserId();
            db.collection("LIKE$$" + id).document(plant.getName()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                System.out.println("user id = "+ id + " " + task.getResult());
                                deleteLiked(id, plant.getName());
                                saveToFavorite(eName, id);
                            } else {
                                System.out.println("Not exist");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast
                                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT).show();
                    })
                    .addOnCompleteListener(task -> {
                        sendUserToActivity();
                    });
        }
    }

    private void deleteLiked(String id, String eName) {
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
            try {
                System.out.println("delete on " + id + " " + eName);
                db1.collection("LIKE$$" + id).document(eName)
                        .delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                System.out.println("Deleted!");
                            } else {
                                System.out.println("Error to delete");
                            }
                        })
                        .addOnFailureListener(e -> System.out.println("Error to delete!"));
            } catch (Exception e) {
                System.out.println(e);
            }
    }

    private void saveToFavorite(String eName, String id) {
        try {
            PlantLiked plantLiked = new PlantLiked(eName);
            db.collection("LIKE$$" + id).document(eName).set(plantLiked)
                    .addOnSuccessListener(unused -> System.out.println("Add to favorite"))
                    .addOnFailureListener(e -> Log.d(TAG, e.toString()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void updatePlant() {
        String eName = name.getText().toString();
        String eSciName = scienceName.getText().toString();
        String eFamily = families.getText().toString();
        String eFamDesc = familyDescription.getText().toString();
        String eBotanical = botanical.getSelectedItem().toString();
        String eDesc = description.getText().toString();
        String eSeason = season.getText().toString();
        String eVitamin = vitamin.getText().toString();
        String eMineral = mineral.getText().toString();
        String eHarvestTime = harvestTime.getText().toString();
        String eTreatment = treatments.getText().toString();
        String eClass = fruit.getSelectedItem().toString();
        String ePlanting = planting.getText().toString();
        String eSoil = soil.getText().toString();
        String eSoilPh = soilPH.getText().toString();
        String eSun = sunExposure.getText().toString();
        String eWater = water.getText().toString();
        String eTemp = temperature.getText().toString();
        String eHumidity = humidity.getText().toString();
        String eFertilizer = fertilizer.getText().toString();

        String eType = plant.getType();

        if (plant.getType().equalsIgnoreCase("VEGETABLE")) {
            if (!eTreatment.equalsIgnoreCase("")) {
                eType += ",HERB";
            }
        } else if (plant.getType().equalsIgnoreCase("FRUIT")) {
            if (!eTreatment.equalsIgnoreCase("")) {
                eType += ",HERB";
            }
        } else if (plant.getType().equalsIgnoreCase("VEGETABLE,HERB") ||
                plant.getType().equalsIgnoreCase("FRUIT,HERB")) {
            if (eTreatment.equalsIgnoreCase("")) {
                eType  = plant.getType().split(",")[0];
            }
        }

        final String eeType = eType;

        if (eName.isEmpty()) {
            name.setError("Enter name field");
        } else if (eSciName.isEmpty()) {
            scienceName.setError("Enter science name");
        } else if (eFamily.isEmpty()) {
            families.setError("Enter families");
        } else if (eFamDesc.isEmpty()) {
            familyDescription.setError("Enter family description");
        } else if (eDesc.isEmpty()) {
            description.setError("Enter description");
        } else if (eSeason.isEmpty()) {
            season.setError("Enter season");
        } else if (eVitamin.isEmpty()) {
            vitamin.setError("Enter vitamin");
        } else if (eMineral.isEmpty()) {
            mineral.setError("Enter mineral");
        } else if (eHarvestTime.isEmpty()) {
            harvestTime.setError("Enter harvest time");
        } else if (ePlanting.isEmpty()) {
            planting.setError("Enter planting");
        } else if (eSoil.isEmpty()) {
            soil.setError("Enter soil");
        } else if (eSoilPh.isEmpty()) {
            soilPH.setError("Enter soil pH");
        } else if (eSun.isEmpty()) {
            sunExposure.setError("Enter sun exposure");
        } else if (eWater.isEmpty()) {
            water.setError("Enter water");
        } else if (eTemp.isEmpty()) {
            temperature.setError("Enter temperature");
        } else if (eHumidity.isEmpty()) {
            humidity.setError("Enter humidity");
        } else if (eFertilizer.isEmpty()) {
            fertilizer.setError("Enter fertilizer");
        } else if (eBotanical == "-- Botanical habit type --") {
            Toast
                    .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Select botanical habit</b></font>"), Toast.LENGTH_SHORT)
                    .show();
        } else if (eClass == "-- Fruit type --" || eClass == "-- Vegetable type --"){
            Toast
                    .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Select classification</b></font>"), Toast.LENGTH_SHORT)
                    .show();
        } else {

            if (filePath != null) {
                progressDialog.setMessage("Please Wait While Update...");
                progressDialog.setTitle("Updated");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                StorageReference ref = storageReference
                        .child(FirebaseLocal.storagePathForImageUpload + "/" + plant.getOwner() + "/" + plant.getType().split(",")[0].toLowerCase() + "s/" + eName);

                ref.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            progressDialog.dismiss();
                            saveUpdate(eName, eSciName, eFamily, eFamDesc, eBotanical, eDesc,
                                    eSeason, eVitamin, eMineral, eHarvestTime, eTreatment, eClass, ePlanting,
                                    eSoil, eSoilPh, eSun, eWater, eTemp, eHumidity, eFertilizer, eeType);
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
                        });
            } else {
                System.out.println("file path = null");
                Toast
                        .makeText(getActivity(), "Please select plant image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void saveUpdate(String eName, String eSciName, String eFamily, String eFamDesc, String eBotanical, String eDesc, String eSeason, String eVitamin, String eMineral, String eHarvestTime, String eTreatment, String eClass, String ePlanting, String eSoil, String eSoilPh, String eSun, String eWater, String eTemp, String eHumidity, String eFertilizer, String eType) {

        Map<String, Object> fruit = new HashMap<>();
        fruit.put(KeyInsert.KEY_NAME, eName);
        fruit.put(KeyInsert.KEY_SCIENCE_NAME, eSciName);
        fruit.put(KeyInsert.KEY_FAMILIES, eFamily);
        fruit.put(KeyInsert.KEY_FAMILY_DESC, eFamDesc);
        fruit.put(KeyInsert.KEY_BOTANICAL_HABIT, eBotanical);
        fruit.put(KeyInsert.KEY_DESCRIPTION, eDesc);
        fruit.put(KeyInsert.KEY_SEASON, eSeason);
        fruit.put(KeyInsert.KEY_VITAMIN, eVitamin);
        fruit.put(KeyInsert.KEY_MINERAL, eMineral);
        fruit.put(KeyInsert.KEY_HARVEST_TIME, eHarvestTime);
        fruit.put(KeyInsert.KEY_TREATMENTS, eTreatment);
        fruit.put(KeyInsert.KEY_PLANTING, ePlanting);
        fruit.put(KeyInsert.KEY_SOIL, eSoil);
        fruit.put(KeyInsert.KEY_SOIL_PH, eSoilPh);
        fruit.put(KeyInsert.KEY_SUN_EXPOSURE, eSun);
        fruit.put(KeyInsert.KEY_WATER, eWater);
        fruit.put(KeyInsert.KEY_TEMPERATURE, eTemp);
        fruit.put(KeyInsert.KEY_HUMIDITY, eHumidity);
        fruit.put(KeyInsert.KEY_FERTILIZER, eFertilizer);
        fruit.put(KeyInsert.TYPE, eType);
        fruit.put(KeyInsert.OWNER, plant.getOwner());

        if (plant.getName().equalsIgnoreCase(eName)) {
            if (plant.getType().equalsIgnoreCase("HERB")) {
                db.collection(plant.getOwner()).document(eName)
                        .update(fruit)
                        .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        });

                db.collection(plant.getType().split(",")[0]).document(eName)
                        .update(fruit)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getActivity(), "update successfully", Toast.LENGTH_SHORT).show();
                            if (!plant.getName().equalsIgnoreCase(eName)) {
                                getAllUsers();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        })
                        .addOnCompleteListener(task -> sendUserToActivity());
            } else {
                fruit.put(KeyInsert.KEY_TYPE, eClass);
                db.collection(plant.getOwner()).document(eName)
                        .update(fruit)
                        .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        });

                db.collection(plant.getType().split(",")[0]).document(eName)
                        .update(fruit)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getActivity(), "update successfully", Toast.LENGTH_SHORT).show();
                            if (!plant.getName().equalsIgnoreCase(eName)) {
                                getAllUsers();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        })
                        .addOnCompleteListener(task -> sendUserToActivity());
            }
        } else {
            if (plant.getType().equalsIgnoreCase("HERB")) {
                db.collection(plant.getType().split(",")[0]).document(eName)
                        .set(fruit)
                        .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        });

                db.collection(plant.getOwner()).document(eName)
                        .set(fruit)
                        .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        });

                db.collection(plant.getOwner()).document(plant.getName())
                        .delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                System.out.println("Deleted!");
                                getAllUsers();
                            } else {
                                System.out.println("Error to delete");
                            }
                        });

                db.collection(plant.getType().split(",")[0]).document(plant.getName())
                        .delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                System.out.println("Deleted!");
                            } else {
                                System.out.println("Error to delete");
                            }
                        });
            } else {
                fruit.put(KeyInsert.KEY_TYPE, eClass);
                db.collection(plant.getType().split(",")[0]).document(eName)
                        .set(fruit)
                        .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        });

                db.collection(plant.getOwner()).document(eName)
                        .set(fruit)
                        .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        });

                db.collection(plant.getOwner()).document(plant.getName())
                        .delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                System.out.println("Deleted!");
                                getAllUsers();
                            } else {
                                System.out.println("Error to delete");
                            }
                        });

                db.collection(plant.getType().split(",")[0]).document(plant.getName())
                        .delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                System.out.println("Deleted!");
                            } else {
                                System.out.println("Error to delete");
                            }
                        });
            }

        }
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    private void sendUserToActivity() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}