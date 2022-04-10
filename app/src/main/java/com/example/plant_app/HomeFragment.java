package com.example.plant_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_app.detail.PlantDetailFragment;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantListView;
import com.example.plant_app.firebase.User;
import com.example.plant_app.profile.EditProfileFragment;
import com.example.plant_app.profile.FavouritePageFragment;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private String userId;
    private TextView helloUser;
    private ImageView iconList, vege1, vege2, vege3, fr1, fr2, fr3, he1, he2, he3, profilePicture;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;

    private User user;

    String[] plantName = new String[] {
            "unknown", "carrot","coriander","cabbage","lettuce","broccoli","madras thorn","bilimbi","santol","pomegranate","salak","pineapple"
            ,"holy basil","roselle","galanga","gotu kola","tamarind","java tea","aloe","andrographis", "amla"
    };
    int[] plantImg = new int[]{
            R.drawable.logo, R.drawable.carrot, R.drawable.coriander, R.drawable.cabbage, R.drawable.lettuce, R.drawable.brocoli, R.drawable.madras_thorn, R.drawable.bilimbi,
            R.drawable.santol, R.drawable.pomegranate, R.drawable.salak, R.drawable.pineapple, R.drawable.holy_basil, R.drawable.roselle, R.drawable.galanga,
            R.drawable.gotu_kola, R.drawable.tamarind, R.drawable.java_tea, R.drawable.aloe, R.drawable.andrographis, R.drawable.amla
    };
    private ArrayList<Plant> homeVegetables = new ArrayList<>();
    private ArrayList<Plant> homeFruits = new ArrayList<>();
    private ArrayList<Plant> homeHerbs = new ArrayList<>();
    int[] vegetableImageIdx = new int[3];
    int[] fruitImageIdx = new int[3];
    int[] herbImageIdx = new int[3];
    private ArrayList<Plant> plantsList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initElement(v);

        initUser(userId);

        getVegetable(v);
        getFruit(v);
        getHerb(v);
        getAllPlants();

        iconList.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(), view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_profile, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.profile_popup_edit:
                        EditProfileFragment editProfileFragment = new EditProfileFragment();
                        editProfileFragment.setUser(user);
                        replaceFragment(editProfileFragment);
                        return true;
                    case R.id.profile_popup_fav:
                        replaceFragment(new FavouritePageFragment());
                        return true;
                    case R.id.profile_popup_regis:
                        replaceFragment(new ProfileFragment());
                        return true;
                    case R.id.profile_popup_logout:
                        FirebaseLocal.logout();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });
        return v;
    }

    private void initElement(View v) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();
        if (firebaseUser == null) {
            navigateToMain();
        }

        storageReference = storage.getReference()
                .child(FirebaseLocal.storagePathForImageUpload + userId + "/profile");
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        System.out.println("downloaded image");
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        profilePicture = v.findViewById(R.id.home_profile_picture);
                        profilePicture.setImageBitmap(bitmap);
                    }).addOnFailureListener(e -> System.out.println(e));
        } catch (Exception e) {
            System.out.println(e);
        }

        userId = firebaseUser.getUid();
        helloUser = v.findViewById(R.id.home_popup);
        iconList = v.findViewById(R.id.profile_list_icon);

        vege1 = v.findViewById(R.id.home_vegetable1);
        vege2 = v.findViewById(R.id.home_vegetable2);
        vege3 = v.findViewById(R.id.home_vegetable3);
        fr1 = v.findViewById(R.id.home_fruit1);
        fr2 = v.findViewById(R.id.home_fruit2);
        fr3 = v.findViewById(R.id.home_fruit3);
        he1 = v.findViewById(R.id.home_herb1);
        he2 = v.findViewById(R.id.home_herb2);
        he3 = v.findViewById(R.id.home_herb3);
    }

    private void getAllPlants() {
        db.collection("VEGETABLE").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            plantsList.add(plant);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());

        db.collection("FRUIT").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            plantsList.add(plant);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());

        db.collection("HERB").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            plantsList.add(plant);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());
    }

    private void getVegetable(View v) {

        db.collection("VEGETABLE").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int count = 0;
                        for (QueryDocumentSnapshot plants : queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            System.out.println(plant);
                            int index = 0;
                            for (int i = 0; i < plantName.length; i++) {
                                if (plant.getName().toLowerCase().equals(plantName[i])) {
                                    index = i;
                                    break;
                                }
                            }
                            homeVegetables.add(plant);
                            if (count == 0) {
                                if (homeVegetables.size() >= 1) {
                                    vege1.setImageResource(plantImg[index]);
                                    vege1.setOnClickListener(view -> toPlantDetail(homeVegetables.get(0)));
                                } else {
                                    vege1.setVisibility(v.GONE);
                                }
                            } else if (count == 1) {
                                if (homeVegetables.size() >= 2) {
                                    vege2.setImageResource(plantImg[index]);
                                    vege2.setOnClickListener(view -> toPlantDetail(homeVegetables.get(1)));
                                } else {
                                    vege2.setVisibility(v.GONE);
                                }
                            } else if (count == 2) {
                                if (homeVegetables.size() >= 3) {
                                    vege3.setImageResource(plantImg[index]);
                                    vege3.setOnClickListener(view -> toPlantDetail(homeVegetables.get(2)));
                                } else {
                                    vege3.setVisibility(v.GONE);
                                }
                            }
                            if (count >= 3) {
                                break;
                            }
                            count++;
                        }
                    }
                }).addOnFailureListener(e -> Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                        .show());
    }

    private void getFruit(View v) {

        db.collection("FRUIT").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int count = 0;
                        for (QueryDocumentSnapshot plants : queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            System.out.println(plant);
                            int index = 0;
                            for (int i = 0; i < plantName.length; i++) {
                                if (plant.getName().toLowerCase().equals(plantName[i])) {
                                    index = i;
                                    break;
                                }
                            }
                            homeFruits.add(plant);
                            if (count == 0) {
                                if (homeFruits.size() >= 1) {
                                    fr1.setImageResource(plantImg[index]);
                                    fr1.setOnClickListener(view -> toPlantDetail(homeFruits.get(0)));
                                } else {
                                    fr1.setVisibility(v.GONE);
                                }
                            } else if (count == 1) {
                                if (homeFruits.size() >= 2) {
                                    fr2.setImageResource(plantImg[index]);
                                    fr2.setOnClickListener(view -> toPlantDetail(homeFruits.get(1)));
                                } else {
                                    fr2.setVisibility(v.GONE);
                                }
                            } else if (count == 2) {
                                if (homeFruits.size() >= 3) {
                                    fr3.setImageResource(plantImg[index]);
                                    fr3.setOnClickListener(view -> toPlantDetail(homeFruits.get(2)));
                                } else {
                                    fr3.setVisibility(v.GONE);
                                }
                            }
                            if (count >= 3) {
                                break;
                            }
                            count++;
                        }
                    }
                }).addOnFailureListener(e -> Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                        .show());
    }

    private void getHerb(View v) {

        db.collection("HERB").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int count = 0;
                        for (QueryDocumentSnapshot plants : queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            System.out.println(plant);
                            int index = 0;
                            for (int i = 0; i < plantName.length; i++) {
                                if (plant.getName().toLowerCase().equals(plantName[i])) {
                                    index = i;
                                    break;
                                }
                            }
                            homeHerbs.add(plant);
                            if (count == 0) {
                                if (homeHerbs.size() >= 1) {
                                    he1.setImageResource(plantImg[index]);
                                    he1.setOnClickListener(view -> toPlantDetail(homeHerbs.get(0)));
                                } else {
                                    he1.setVisibility(v.GONE);
                                }
                            } else if (count == 1) {
                                if (homeHerbs.size() >= 2) {
                                    he2.setImageResource(plantImg[index]);
                                    he2.setOnClickListener(view -> toPlantDetail(homeHerbs.get(1)));
                                } else {
                                    he2.setVisibility(v.GONE);
                                }
                            } else if (count == 2) {
                                if (homeHerbs.size() >= 3) {
                                    he3.setImageResource(plantImg[index]);
                                    he3.setOnClickListener(view -> toPlantDetail(homeHerbs.get(2)));
                                } else {
                                    he3.setVisibility(v.GONE);
                                }
                            }
                            if (count >= 3) {
                                break;
                            }
                            count++;
                        }
                    }
                }).addOnFailureListener(e -> Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                        .show());
    }

    private void navigateToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
    }

    private void initUser(String id) {
        DocumentReference db = FirebaseFirestore.getInstance().collection("User").document(id);
        db.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            user = document.toObject(User.class);
                            helloUser.setText("Hello,\n" + user.getFirstname() + " " + user.getLastname());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void toPlantDetail(Plant plant) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlantDetailFragment plantDetailFragment = new PlantDetailFragment();
        plantDetailFragment.setPlant(plant);
        plantDetailFragment.setAllPlants(plantsList);

        fragmentTransaction.replace(R.id.homeFrameLayout, plantDetailFragment);
        fragmentTransaction.commit();
    }
}