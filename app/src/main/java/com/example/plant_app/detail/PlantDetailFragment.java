package com.example.plant_app.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_app.HomeActivity;
import com.example.plant_app.MainActivity;
import com.example.plant_app.R;
import com.example.plant_app.edit_plant.EditPlantFragment;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantLiked;
import com.example.plant_app.firebase.PlantListView;
import com.example.plant_app.firebase.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlantDetailFragment extends Fragment {

    private static final String TAG = "PlantDetailFragment";

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference, ref;
    private FirebaseFirestore db;
    private String userId;
    private User user;

    Plant plantArg;
    private List<Plant> plantsList;
    private List<User> allUser = new ArrayList<>();
    private TextView pName, pSciName, pFamily, pFamDescription, pHabit, pDescription, pSeason, pVitamin,
    pMineral, pTreatment, pHarvestTime, pPlanting, pSoil, pSoilPH, pSun, pWater, pTemp, pHumi, pFert;
    private ImageView favourite, pImage, arrowHarvest, arrowPlanting, arrowPlantCare;
    private LinearLayout pLinearHarvest, pLinearPlanting, pLinearPlantCare, pLinearPlantCareAll, pLinearTreatment;
    private Button ontology, adminEdit, adminDelete;
    private boolean checkIsFavoritePlant = false;

    public PlantDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plant_detail, container, false);

        System.out.println("hello plant detail page = " + plantArg);

        initElement(v);

        initText(v);

        checkUserLiked();

        getImage();

        pLinearHarvest.setOnClickListener(view -> setHarvestVisibility(v));

        pLinearPlanting.setOnClickListener(view -> setPlantingVisibility(v));

        pLinearPlantCare.setOnClickListener(view -> setPlantCareVisibility(v));

        favourite.setOnClickListener(view -> {
            if (checkIsFavoritePlant) {
                setToUnFavorite();
                checkIsFavoritePlant = false;
            } else {
                saveToFavorite();
                checkIsFavoritePlant = true;
            }
        });
        
        adminDelete.setOnClickListener(view -> getAllUsers());
        adminEdit.setOnClickListener(view -> editPlant());

        ontology.setOnClickListener(view -> seeOntology(plantArg));

        return v;
    }

    private void editPlant() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EditPlantFragment editPlantFragment = new EditPlantFragment();
        editPlantFragment.setPlant(plantArg);
        fragmentTransaction.replace(R.id.homeFrameLayout, editPlantFragment);
        fragmentTransaction.commit();
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
                    deletePlant();
                });

    }

    private void deletePlant() {
        System.out.println("size of user = " + allUser.size());
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        for (int i = 0; i < allUser.size(); i++) {
            try {
                System.out.println("delete on " + allUser.get(i).getFirstname() + " " + plantArg.getName());
                db1.collection("LIKE$$" + allUser.get(i).getUserId()).document(plantArg.getName())
                    .delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                System.out.println("Deleted!");
                            } else {
                                System.out.println("Error to delete");
                            }
                        });
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        for (int i = 0; i < allUser.size(); i++) {
            try {
                System.out.println("delete on " + allUser.get(i).getFirstname() + " " + plantArg.getName());
                db2.collection(allUser.get(i).getUserId()).document(plantArg.getName())
                        .delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                System.out.println("Deleted!");
                            } else {
                                System.out.println("Error to delete");
                            }
                        });
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        DocumentReference db3 = FirebaseFirestore.getInstance().collection(plantArg.getType().split(",")[0]).document(plantArg.getName());
        db3.delete()
                .addOnCompleteListener(task -> {
                    System.out.println("Remove complete");
                    sendUserToActivity();
                });
    }

    private void seeOntology(Plant plant) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        OntologyFragment ontologyFragment = new OntologyFragment();
        ontologyFragment.setPlant(plant);
        ontologyFragment.setAllPlants(plantsList);

        fragmentTransaction.replace(R.id.homeFrameLayout, ontologyFragment);
        fragmentTransaction.commit();
    }

    private void setHarvestVisibility(View v){
        if (pHarvestTime.getVisibility() == v.VISIBLE) {
            arrowHarvest.setImageResource(R.drawable.ic_round_keyboard_arrow_down_24);
            pHarvestTime.setVisibility(v.GONE);
        } else {
            arrowHarvest.setImageResource(R.drawable.ic_round_keyboard_arrow_up_24);
            pHarvestTime.setVisibility(v.VISIBLE);
        }
    }

    private void setPlantingVisibility(View v){
        if (pPlanting.getVisibility() == v.VISIBLE) {
            arrowPlanting.setImageResource(R.drawable.ic_round_keyboard_arrow_down_24);
            pPlanting.setVisibility(v.GONE);
        } else {
            arrowPlanting.setImageResource(R.drawable.ic_round_keyboard_arrow_up_24);
            pPlanting.setVisibility(v.VISIBLE);
        }
    }

    private void setPlantCareVisibility(View v){
        if (pLinearPlantCareAll.getVisibility() == v.VISIBLE) {
            arrowPlantCare.setImageResource(R.drawable.ic_round_keyboard_arrow_down_24);
            pLinearPlantCareAll.setVisibility(v.GONE);
        } else {
            arrowPlantCare.setImageResource(R.drawable.ic_round_keyboard_arrow_up_24);
            pLinearPlantCareAll.setVisibility(v.VISIBLE);
        }
    }

    private void initText(View v) {
        pName.setText(plantArg.getName());
        pSciName.setText(plantArg.getScienceName());
        pFamily.setText(plantArg.getFamilies());
        pFamDescription.setText(plantArg.getFamilyDescription());
        pHabit.setText(plantArg.getBotanicalHabit());
        pDescription.setText(plantArg.getDescription());
        pSeason.setText(plantArg.getSeason());
        pVitamin.setText(plantArg.getVitamin());
        pMineral.setText(plantArg.getMineral());
        pTreatment.setText(plantArg.getTreatments());
        pHarvestTime.setText(plantArg.getHarvestTime());
        pSoil.setText(plantArg.getSoil());
        pSoilPH.setText(plantArg.getSoilPH());
        pSun.setText(plantArg.getSunExposure());
        pWater.setText(plantArg.getWater());
        pTemp.setText(plantArg.getTemperature());
        pHumi.setText(plantArg.getHumidity());
        pFert.setText(plantArg.getFertilizer());
        pPlanting.setText(plantArg.getPlanting());
    }

    private void initElement(View v) {
        pName = v.findViewById(R.id.plant_detail_name);
        pSciName = v.findViewById(R.id.plant_detail_sciname);
        pFamily = v.findViewById(R.id.plant_detail_family);
        pFamDescription = v.findViewById(R.id.plant_detail_family_desc);
        pHabit = v.findViewById(R.id.plant_detail_habit);
        pDescription = v.findViewById(R.id.plant_detail_description);
        pSeason = v.findViewById(R.id.plant_detail_season);
        pVitamin = v.findViewById(R.id.plant_detail_vitamin);
        pMineral = v.findViewById(R.id.plant_detail_mineral);
        pTreatment = v.findViewById(R.id.plant_detail_treatments);
        pHarvestTime = v.findViewById(R.id.plant_detail_harvest_time);
        pPlanting = v.findViewById(R.id.plant_detail_planting);
        pSoil = v.findViewById(R.id.plant_detail_soil);
        pSoilPH = v.findViewById(R.id.plant_detail_soilPH);
        pSun = v.findViewById(R.id.plant_detail_sun_exposure);
        pWater = v.findViewById(R.id.plant_detail_water);
        pTemp = v.findViewById(R.id.plant_detail_temperature);
        pHumi = v.findViewById(R.id.plant_detail_humidity);
        pFert = v.findViewById(R.id.plant_detail_fertilizer);
        pLinearHarvest = v.findViewById(R.id.plant_detail_linear_harvest_time);
        pLinearPlantCare = v.findViewById(R.id.plant_detail_linear_plant_care);
        pLinearPlanting = v.findViewById(R.id.plant_detail_linear_planting);
        pLinearPlantCareAll = v.findViewById(R.id.plant_detail_linear_plant_care_all);
        pLinearTreatment = v.findViewById(R.id.plant_detail_linear_treatment);

        ontology = v.findViewById(R.id.plant_detail_button_ontology);

        favourite = v.findViewById(R.id.plant_detail_like);
        pImage = v.findViewById(R.id.plant_detail_top_image);
        arrowHarvest = v.findViewById(R.id.plant_detail_arrow_harvest_time);
        arrowPlantCare = v.findViewById(R.id.plant_detail_arrow_plant_care);
        arrowPlanting = v.findViewById(R.id.plant_detail_arrow_planting);

        pHarvestTime.setVisibility(v.GONE);
        pPlanting.setVisibility(v.GONE);
        pLinearPlantCareAll.setVisibility(v.GONE);
        if (plantArg.getTreatments().equalsIgnoreCase("")) {
            pLinearTreatment.setVisibility(v.GONE);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        if (firebaseUser == null) {
            navigateToMain();
        }

        adminEdit = v.findViewById(R.id.plant_detail_btn_admin_edit);
        adminDelete = v.findViewById(R.id.plant_detail_btn_admin_delete);

        adminEdit.setVisibility(v.GONE);
        adminDelete.setVisibility(v.GONE);

        initUser(userId, v);
    }

    private void saveToFavorite() {
        PlantLiked plantLiked = new PlantLiked(plantArg.getName());
        db.collection("LIKE$$" + userId).document(plantArg.getName()).set(plantLiked)
                .addOnSuccessListener(unused -> {
                    favourite.setImageResource(R.drawable.ic_round_favorite_24_red);
                    checkIsFavoritePlant = true;
                    Toast.makeText(getActivity(), "Add to your favorite", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                });
    }

    private void setToUnFavorite() {
        db.collection("LIKE$$" + userId).document(plantArg.getName()).delete()
                .addOnSuccessListener(unused -> {
                    favourite.setImageResource(R.drawable.ic_round_favorite_border_24);
                    Toast.makeText(getActivity(), "Remove from favorite!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Error to dislike!</b></font>"), Toast.LENGTH_SHORT)
                        .show());
    }

    public void checkUserLiked() {
        db.collection("LIKE$$" + userId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        boolean check = false;
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            System.out.println("hello check  " + plants);
                            PlantLiked plantLiked = plants.toObject(PlantLiked.class);
                            if (plantLiked.getName().equalsIgnoreCase(plantArg.getName())) {
                                check = true;
                                break;
                            }
                        }
                        if (check == false) {
                            favourite.setImageResource(R.drawable.ic_round_favorite_border_24);
                        }
                    } else {
                        favourite.setImageResource(R.drawable.ic_round_favorite_border_24);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT).show();
                });
    }

    private void getImage() {
        ref = storage.getReference()
                .child(FirebaseLocal.storagePathForImageUpload + plantArg.getOwner() + "/" + plantArg.getType().split(",")[0].toLowerCase() + "s/" + plantArg.getName());
        System.out.println("plant type = " + plantArg.getType().split(",")[0]);
        try {
            final File localFile = File.createTempFile(plantArg.getName(), "jpg");
            ref.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        System.out.println("downloaded image");
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        pImage.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(e -> System.out.println(e));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void initUser(String id, View v) {
        DocumentReference db = FirebaseFirestore.getInstance().collection("User").document(id);
        db.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            user = document.toObject(User.class);
                            if (user.getStatus().equalsIgnoreCase("admin") || user.getUserId().equalsIgnoreCase(plantArg.getOwner())) {
                                adminEdit.setVisibility(v.VISIBLE);
                                adminDelete.setVisibility(v.VISIBLE);
                            } else {
                                adminEdit.setVisibility(v.GONE);
                                adminDelete.setVisibility(v.GONE);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                });
    }

    public void setPlant(Plant plant) {
        this.plantArg = plant;
    }
    
    public void setAllPlants(List<Plant> plantsList) {
        this.plantsList = new ArrayList<>(plantsList);
    }

    private void navigateToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
    }

    private void sendUserToActivity() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}