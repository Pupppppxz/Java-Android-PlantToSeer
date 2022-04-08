package com.example.plant_app.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.plant_app.MainActivity;
import com.example.plant_app.ProfileFragment;
import com.example.plant_app.R;
import com.example.plant_app.detail.PlantDetailFragment;
import com.example.plant_app.detail.RecyclerItemClickListener;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantLiked;
import com.example.plant_app.firebase.PlantListView;
import com.example.plant_app.profile.EditProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FavouritePageFragment extends Fragment {

    private static final String TAG = "FavouritePageFragment";
    private ImageView iconList;
    private String userId;
    private TextView helloUser;
    private RecyclerView vegetableRecycle;
    private RecyclerView fruitRecycle;
    private RecyclerView herbRecycle;
    ImageAdapter vegetableAdapter;
    ImageAdapter fruitAdapter;
    ImageAdapter herbAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference, ref;
    private FirebaseFirestore db;

    String[] plantName = new String[] {
            "unknown", "carrot","coriander","cabbage","lettuce","broccoli","madras thorn","bilimbi","santol","pomegranate","salak","pineapple"
            ,"holy basil","roselle","galanga","gotu kola","tamarind","java tea","aloe","andrographis"
    };
    int[] plantImg = new int[]{
            R.drawable.logo, R.drawable.carrot, R.drawable.coriander, R.drawable.cabbage, R.drawable.lettuce, R.drawable.brocoli, R.drawable.madras_thorn, R.drawable.bilimbi,
            R.drawable.santol, R.drawable.pomegranate, R.drawable.salak, R.drawable.pineapple, R.drawable.holy_basil, R.drawable.roselle, R.drawable.galanga,
            R.drawable.gotu_kola, R.drawable.tamarind, R.drawable.java_tea, R.drawable.aloe, R.drawable.andrographis
    };
    List<PlantListView> profileVegetables = new ArrayList<>();
    List<PlantListView> profileFruits = new ArrayList<>();
    List<PlantListView> profileHerbs = new ArrayList<>();
    private List<Plant> plantsList = new ArrayList<>();

    public FavouritePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite_page, container, false);

        initElement(v);

        initUser(userId);

        getAllPlant(v);

        iconList.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(), view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_profile, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.profile_popup_edit:
                        replaceFragment(new EditProfileFragment());
                        return true;
                    case R.id.profile_popup_fav:
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
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        if (firebaseUser == null) {
            navigateToMain();
        }

        vegetableRecycle = v.findViewById(R.id.favorite_vegetable_recycle);
        fruitRecycle = v.findViewById(R.id.favorite_fruit_recycle);
        herbRecycle = v.findViewById(R.id.favorite_herb_recycle);
        helloUser = v.findViewById(R.id.favorite_user_name);
        iconList = v.findViewById(R.id.favorite_list_icon);
    }

    private void getAllPlant(View v) {
        List<PlantLiked> plantLikeds = new ArrayList<>();

        db.collection("LIKE$$" + userId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants : queryDocumentSnapshots) {
                            PlantLiked plant = plants.toObject(PlantLiked.class);
                            plantLikeds.add(plant);
                        }
                        getFruit(v, plantLikeds);
                        getVegetable(v, plantLikeds);
                        getHerb(v, plantLikeds);
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());
    }

    private void getHerb(View v, List<PlantLiked> plantLikeds) {
        db.collection("HERB").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<PlantListView> pHerb = new ArrayList<>();
                        int count = 0;
                        for (QueryDocumentSnapshot plants : queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            int index = 0;
                            for (int i = 0; i < plantLikeds.size(); i++) {
                                if (plantLikeds.get(i).getName().equalsIgnoreCase(plant.getName())) {
                                    for (int j = 0; j < plantName.length; j++) {
                                        if (plant.getName().toLowerCase().equals(plantName[j])) {
                                            index = j;
                                            break;
                                        }
                                    }
                                    PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index], count);
                                    pHerb.add(plantListView);
                                    break;
                                }
                            }
                            count++;
                            plantsList.add(plant);
                        }
                        System.out.println("size 3 array list favorite = " + pHerb.size());
                        if (pHerb.size() > 0) {
                            this.profileHerbs = new ArrayList<>(pHerb);
                            initHerbAdapter(v);
                        } else {
                            herbRecycle.setVisibility(v.GONE);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());
    }

    private void getVegetable(View v, List<PlantLiked> plantLikeds) {
        db.collection("VEGETABLE").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<PlantListView> pVege = new ArrayList<>();
                        int count = 0;
                        for (QueryDocumentSnapshot plants : queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            int index = 0;
                            for (int i = 0; i < plantLikeds.size(); i++) {
                                if (plantLikeds.get(i).getName().equalsIgnoreCase(plant.getName())) {
                                    for (int j = 0; j < plantName.length; j++) {
                                        if (plant.getName().toLowerCase().equals(plantName[j])) {
                                            index = j;
                                            break;
                                        }
                                    }
                                    PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index], count);
                                    pVege.add(plantListView);
                                    break;
                                }
                            }
                            count++;
                            plantsList.add(plant);
                        }
                        System.out.println("size 3 array list favorite = " + pVege.size());
                        if (pVege.size() > 0) {
                            this.profileVegetables = new ArrayList<>(pVege);
                            initVegetableAdapter(v);
                        } else {
                            vegetableRecycle.setVisibility(v.GONE);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());
    }

    private void getFruit(View v, List<PlantLiked> plantLikeds) {
        db.collection("FRUIT").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int count = 0;
                        List<PlantListView> pFruit = new ArrayList<>();
                        for (QueryDocumentSnapshot plants : queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            int index = 0;
                            for (int i = 0; i < plantLikeds.size(); i++) {
                                if (plantLikeds.get(i).getName().equalsIgnoreCase(plant.getName())) {
                                    for (int j = 0; j < plantName.length; j++) {
                                        if (plant.getName().toLowerCase().equals(plantName[j])) {
                                            index = j;
                                            break;
                                        }
                                    }
                                    PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index], count);
                                    pFruit.add(plantListView);
                                    break;
                                }
                            }
                            count++;
                            plantsList.add(plant);
                        }
                        System.out.println("size 3 array list favorite = " + pFruit.size());
                        if (pFruit.size() > 0) {
                            this.profileFruits = new ArrayList<>(pFruit);
                            initFruitAdapter(v);
                        } else {
                            fruitRecycle.setVisibility(v.GONE);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());
    }

    private void viewPlantDetail(String name) {

        PlantDetailFragment plantDetailFragment = new PlantDetailFragment();
        for (int i = 0; i < plantsList.size(); i++) {
            if (plantsList.get(i).getName().equalsIgnoreCase(name)) {
                plantDetailFragment.setPlant(plantsList.get(i));
            }
        }
        plantDetailFragment.setAllPlants(plantsList);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.homeFrameLayout, plantDetailFragment);
        fragmentTransaction.commit();
    }

    private void initVegetableAdapter(View v) {

        System.out.println("Init adapter");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        vegetableAdapter = new ImageAdapter(profileVegetables);
        vegetableRecycle.setLayoutManager(mLayoutManager);
        vegetableRecycle.setItemAnimator(new DefaultItemAnimator());
        vegetableRecycle.setAdapter(vegetableAdapter);
        vegetableRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), vegetableRecycle, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPlantDetail(profileVegetables.get(position).getName());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void initFruitAdapter(View v) {

        System.out.println("Init adapter");
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        fruitAdapter = new ImageAdapter(profileFruits);
        fruitRecycle.setLayoutManager(mLayoutManager1);
        fruitRecycle.setItemAnimator(new DefaultItemAnimator());
        fruitRecycle.setAdapter(fruitAdapter);
        fruitRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), fruitRecycle, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPlantDetail(profileFruits.get(position).getName());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void initHerbAdapter(View v) {

        System.out.println("Init adapter");
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        herbAdapter = new ImageAdapter(profileHerbs);
        herbRecycle.setLayoutManager(mLayoutManager2);
        herbRecycle.setItemAnimator(new DefaultItemAnimator());
        herbRecycle.setAdapter(herbAdapter);
        herbRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), herbRecycle, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPlantDetail(profileHerbs.get(position).getName());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void initUser(String id) {
        DocumentReference db = FirebaseFirestore.getInstance().collection("User").document(id);
        db.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            String fullname = "Hello,\n" + document.getString("firstname") + " " + document.getString("lastname");
                            helloUser.setText(fullname);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                });
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