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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.plant_app.detail.RecyclerItemClickListener;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantListView;
import com.example.plant_app.firebase.User;
import com.example.plant_app.profile.EditProfileFragment;
import com.example.plant_app.profile.FavouritePageFragment;
import com.example.plant_app.profile.ImageAdapter;
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
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private String userId;
    private TextView helloUser;
    private ImageView iconList, vege1, vege2, vege3, fr1, fr2, fr3, he1, he2, he3, profilePicture;

    private RecyclerView vegetableRecycle;
    private RecyclerView fruitRecycle;
    private RecyclerView herbRecycle;
    ImageAdapter vegetableAdapter;
    ImageAdapter fruitAdapter;
    ImageAdapter herbAdapter;
    LinearLayoutManager mLayoutManager, mLayoutManager1, mLayoutManager2;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference, ref;
    private FirebaseFirestore db;

    private User user;
    List<PlantListView> homeVegetables = new ArrayList<>();
    List<PlantListView> homeFruits = new ArrayList<>();
    List<PlantListView> homeHerbs = new ArrayList<>();
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
        getAllPlants(v);

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

        helloUser = v.findViewById(R.id.home_popup);
        iconList = v.findViewById(R.id.profile_list_icon);

        storageReference = storage.getReference()
                .child(FirebaseLocal.storagePathForImageUpload + userId + "/profile");
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        try {
                            System.out.println("downloaded image");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profilePicture = v.findViewById(R.id.home_profile_picture);
                            profilePicture.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }).addOnFailureListener(e -> System.out.println(e));
        } catch (Exception e) {
            System.out.println(e);
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        vegetableRecycle = v.findViewById(R.id.home_vegetable_recycle);

        mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        fruitRecycle = v.findViewById(R.id.home_fruit_recycle);

        mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        herbRecycle = v.findViewById(R.id.home_herb_recycle);

    }

    private void getAllPlants(View v) {
        db.collection("VEGETABLE").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            plantsList.add(plant);
                            if (homeVegetables.size() <= 3) {
                                getVegetableImage(plant.getName(), plant.getScienceName(), plant.getType(), plant.getTreatments(), v, plant.getOwner());
                            }
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
                            if (homeFruits.size() <= 3) {
                                getFruitImage(plant.getName(), plant.getScienceName(), plant.getType(), plant.getTreatments(), v, plant.getOwner());
                            }
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
                            if (homeHerbs.size() <= 3) {
                                getHerbImage(plant.getName(), plant.getScienceName(), plant.getType(), plant.getTreatments(), v, plant.getOwner());
                            }
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());
    }

    private void getVegetableImage(String plantName, String sciName, String type, String treatment, View v, String id) {
        ref = storage.getReference()
                .child(FirebaseLocal.storagePathForImageUpload + id + "/vegetables/" + plantName);
        try {
            final File localFile = File.createTempFile(plantName, "jpg");
            ref.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        System.out.println("downloaded image");
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        PlantListView plantListView = new PlantListView(plantName, sciName, type, bitmap, 0, treatment);
                        if (homeVegetables.size() <= 3) {
                            homeVegetables.add(plantListView);
                        }
                    })
                    .addOnFailureListener(e -> System.out.println(e))
                    .addOnCompleteListener(task -> {
                        initVegetableAdapter(v);
                    });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getFruitImage(String plantName, String sciName, String type, String treatment, View v, String id) {
        ref = storage.getReference()
                .child(FirebaseLocal.storagePathForImageUpload + id + "/fruits/" + plantName);
        try {
            final File localFile = File.createTempFile(plantName, "jpg");
            ref.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        System.out.println("downloaded image");
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        PlantListView plantListView = new PlantListView(plantName, sciName, type, bitmap, 0, treatment);
                        if (homeFruits.size() <= 3) {
                            homeFruits.add(plantListView);
                        }
                    })
                    .addOnFailureListener(e -> System.out.println(e))
                    .addOnCompleteListener(task -> initFruitAdapter(v));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getHerbImage(String plantName, String sciName, String type, String treatment, View v, String id) {
        ref = storage.getReference()
                .child(FirebaseLocal.storagePathForImageUpload + id + "/herbs/" + plantName);
        try {
            final File localFile = File.createTempFile(plantName, "jpg");
            ref.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        System.out.println("downloaded image");
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        PlantListView plantListView = new PlantListView(plantName, sciName, type, bitmap, 0, treatment);
                        if (homeHerbs.size() <= 3) {
                            homeHerbs.add(plantListView);
                        }
                    })
                    .addOnFailureListener(e -> System.out.println(e))
                    .addOnCompleteListener(task -> initHerbAdapter(v));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void initFruitAdapter(View v) {
        try {
            if (homeFruits.size() > 0) {

                fruitAdapter = new ImageAdapter(homeFruits);
                fruitRecycle.setLayoutManager(mLayoutManager1);
                fruitRecycle.setItemAnimator(new DefaultItemAnimator());
                fruitRecycle.setAdapter(fruitAdapter);
                fruitRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), fruitRecycle, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        viewPlantDetail(homeFruits.get(position).getName());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void initHerbAdapter(View v) {
        try {
            if (homeHerbs.size() > 0) {
                herbAdapter = new ImageAdapter(homeHerbs);
                herbRecycle.setLayoutManager(mLayoutManager2);
                herbRecycle.setItemAnimator(new DefaultItemAnimator());
                herbRecycle.setAdapter(herbAdapter);
                herbRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), herbRecycle, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        viewPlantDetail(homeHerbs.get(position).getName());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void initVegetableAdapter(View v) {

        try {
            if (homeVegetables.size() > 0) {

                vegetableAdapter = new ImageAdapter(homeVegetables);
                vegetableRecycle.setLayoutManager(mLayoutManager);
                vegetableRecycle.setItemAnimator(new DefaultItemAnimator());
                vegetableRecycle.setAdapter(vegetableAdapter);
                vegetableRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), vegetableRecycle, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        viewPlantDetail(homeVegetables.get(position).getName());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
    }

    private void initUser(String id) {
        System.out.println("user id = " + id);
        DocumentReference db = FirebaseFirestore.getInstance().collection("User").document(id);
        db.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            user = document.toObject(User.class);
                            System.out.println(user);
                            try {
                                helloUser.setText("Hello,\n" + user.getFirstname() + " " + user.getLastname());
                            } catch (Exception e) {
                                System.out.println("Error to get user");
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                });
        System.out.println(user);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void viewPlantDetail(String name) {

        System.out.println("click on = " + name);

        PlantDetailFragment plantDetailFragment = new PlantDetailFragment();
        for (int i = 0; i < plantsList.size(); i++) {
            System.out.println(plantsList.get(i));
            if (plantsList.get(i).getName().equalsIgnoreCase(name)) {
                plantDetailFragment.setPlant(plantsList.get(i));
                break;
            }
        }
        plantDetailFragment.setAllPlants(plantsList);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.homeFrameLayout, plantDetailFragment);
        fragmentTransaction.commit();
    }
}